# Releasing Operator

Operator is distributed through [IzzyOnDroid](https://apt.izzysoft.de/packages/com.byarchitect.operator),
which verifies that the published APK can be rebuilt byte-for-byte from the tagged source
(see "RB Hints for Developers" in the [IzzyOnDroid wiki](https://gitlab.com/IzzyOnDroid/repo/-/wikis/home)).
The steps below exist so that stays true.

## The first basic rule

**Always build from a clean tree, at exactly the commit the tag points to.**

The v1.0.0 release broke this rule: the published APK was built before the release commit
was made, so it shipped with `versionName = "0.0.7"` while the tag said `v1.0.0`. Building
from the tag would have caught it.

## Release checklist

1. **Bump the version** in `app/build.gradle.kts`:
   - `versionCode` — always increase by at least 1. Android only treats a build as an
     update when its `versionCode` is higher than the installed one.
   - `versionName` — the human-readable version, matching the tag without the `v` prefix.
2. **Add a changelog** at `metadata/en-US/changelogs/<versionCode>.txt` (max 500 characters).
   IzzyOnDroid and F-Droid read this file to show "What's new".
3. **Commit** the version bump, then **tag that exact commit**: `git tag -a v1.0.1 -m "v1.0.1"`.
4. **Push** the commit and the tag: `git push && git push --tags`.
5. **Build from a clean checkout of the tag** — not from your working tree:

   ```bash
   git clone --branch v1.0.1 --depth 1 https://github.com/by-architect/Operator.git operator-release
   cd operator-release
   ./gradlew clean assembleRelease
   ```

6. **Verify the built APK reports the right version** before publishing anything:

   ```bash
   aapt dump badging app/build/outputs/apk/release/app-release.apk | head -1
   # expect: versionCode='2' versionName='1.0.1'
   ```

7. **Sign** the APK with the release keystore (kept outside the repository), then attach it
   to a GitHub release created from that same tag.

## How IzzyOnDroid picks up a release

Nothing is submitted by hand. IzzyOnDroid scans this repository and pulls new
releases by itself, so the release process *is* the publishing process.

**It watches tags and GitHub releases — not `master`.** Pushing a commit to
`master` will never put anything in the store. The trigger is a new tag with a
GitHub release attached to it.

What has to line up for the scan to succeed:

| | Requirement |
| --- | --- |
| Tag | `v<versionName>` — e.g. `v1.0.1` |
| Release asset | `Operator-v<versionName>.apk` — the name is matched by pattern, so keep the shape exactly |
| `versionCode` | must be higher than the previous release |
| Signature | must be the key in `secrets/operator-release-2026.keystore` (`8c74ae8f…`), which is whitelisted on their side |
| Changelog | `metadata/en-US/changelogs/<versionCode>.txt` — imported via Fastlane and shown as "What's New" |
| Build | must reproduce from the tag; they rebuild and compare against the published APK |

Pickup is not instant. The scan runs periodically, the result goes to staging,
and it appears publicly at the next sync — hours rather than minutes.

If a release does not show up, the usual causes are, in order: the APK was not
attached to the release, the asset name does not match the pattern, the
`versionCode` did not increase, or the APK was signed with the wrong key.

## Never change what has already been distributed

Once an APK is published, it stays as it is. If a release is wrong, do **not** replace the
file on an existing release — cut a new release with a higher `versionCode` instead. The only
exceptions are pulling an APK that is broken or a security risk.

## Signing key

The signing key was replaced on 2026-09-24. The original `operator-release.keystore` had been
committed to this repository since 2025-11-10 and is still downloadable from a fork, so it must
be treated as compromised. It was purged from this repository's history and a new key generated.

**The password for the old keystore is lost**, along with the machine it was created on. That
rules out APK Signature Scheme v3 certificate rotation, which would have kept updates working:
building a rotation lineage requires the old private key. So v1.0.1 is a clean break.

### Consequence for users

v1.0.1 is signed by a different certificate than v1.0.0, so Android refuses to install it as an
update (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`). **Users must uninstall v1.0.0 first**, which
loses their settings. This is unavoidable and is stated in the changelog, the README and the
release notes.

It also means the repository's entry in any F-Droid-style index has a pinned signer
(`AllowedAPKSigningKeys`) that no longer matches. Those entries must be updated by the repo
maintainers before v1.0.1 can be published there.

### Signing a release

```bash
zipalign -p -f 4 app-release-unsigned.apk aligned.apk
apksigner sign --ks "$KEYDIR/operator-release-2026.keystore" --ks-key-alias operator \
  --out Operator-<version>.apk aligned.apk
apksigner verify --print-certs Operator-<version>.apk
```

### What must never be lost again

`operator-release-2026.keystore` **and its password**. Losing either repeats exactly what
happened here: every user has to uninstall and reinstall. Keep the keystore backed up offline
and the password in a password manager — not only in an IDE's "remember passwords" box, which
is what failed this time. The keystore must never be committed here; `.gitignore` covers
`*.keystore` / `*.jks`. See "How to keep your key safe" in the
[IzzyOnDroid wiki](https://gitlab.com/IzzyOnDroid/repo/-/wikis/home).
