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

## Never change what has already been distributed

Once an APK is published, it stays as it is. If a release is wrong, do **not** replace the
file on an existing release — cut a new release with a higher `versionCode` instead. The only
exceptions are pulling an APK that is broken or a security risk.

## Signing key

The release keystore must never be committed to this repository (`.gitignore` covers
`*.keystore` / `*.jks`). Keep an offline backup of the keystore and its passwords: losing it
means the app can no longer be updated under the same signature, and users would have to
uninstall and reinstall. See "How to keep your key safe" in the
[IzzyOnDroid wiki](https://gitlab.com/IzzyOnDroid/repo/-/wikis/home).
