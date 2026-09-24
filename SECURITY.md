# Security

## Contact

For security issues, key verification, or anything that needs an out-of-band
channel — for example confirming that a release really came from me:

**byarchitect@disroot.org**

> Until the handover is complete, `emindemir1541@proton.me` also reaches me.
> That address is being retired and will be removed from this file once the
> new one is confirmed.

Please use email rather than a public issue for anything sensitive.

## Release signing

Releases are signed by hand and published on the
[Releases](https://github.com/by-architect/Operator/releases) page and through
[IzzyOnDroid](https://apt.izzysoft.de/packages/com.byarchitect.operator).

Current signing certificate (SHA-256):

```
8c74ae8fd94d7122d50d6d2db3f50b79be06e57b82aee1d07e6a89c45e14895e
```

Verify any APK you download against it:

```bash
apksigner verify --print-certs Operator-<version>.apk
```

## Signing key history

**v1.0.0 and earlier** were signed with a different key, which must be treated as
**compromised**: the keystore was accidentally committed to this repository and
was publicly downloadable from 2025-11-10 until 2026-09-24. Its password was
subsequently lost, so the key could not be used to sign a compatible update via
APK Signature Scheme v3 certificate rotation.

The old keystore has been purged from this repository's history, and v1.0.1
onwards is signed with the new key above. This is why v1.0.1 cannot install as
an update over v1.0.0 — see the release notes.

Do not trust any APK signed with the old certificate
(`046e269cfdb4816b4ef3d554a63de6be063e40008b524c04397d3b04315eaccd`).
IzzyOnDroid is adding it to their block list.

See [`RELEASING.md`](RELEASING.md) for how releases are built and verified.
