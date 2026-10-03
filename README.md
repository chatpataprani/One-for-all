# HAO

HAO is a glass-style Android toolkit by **Chatpataprani** for identifier lookup, validation, security utilities, file inspection, image/PDF workflows, text utilities, and official UIDAI handoffs.

## Android app

- Native Kotlin + Jetpack Compose
- Edge-to-edge layout with light/dark themes
- Readable system typography and low-glare glass surfaces
- Home, Search, and Tools navigation
- Named tool cards with icons and category filtering
- Local search history and configurable haptics
- Number and Aadhaar lookup through the configured live lookup service
- Aadhaar format/checksum validation
- Age/date calculation
- Password generation and hashing
- PAN, GSTIN, IMEI and MAC validation
- CDR/IPDR text analysis helpers
- QR, image, PDF, metadata and file-tool workspaces
- Official UIDAI OTP flow opens on the UIDAI website; HAO does not collect OTPs

## Build

Requirements:

- Android SDK 35
- JDK 17
- Gradle 8.9+

Build the debug APK:

```bash
cd android
gradle assembleDebug
```

The APK is written to `android/app/build/outputs/apk/debug/app-debug.apk`.

## Source layout

- `android/app/src/main/java/com/chatpataprani/hao/` — Android source
- `android/app/src/main/AndroidManifest.xml` — app manifest
- `.github/workflows/build.yml` — Android CI build
- `README.md` — project documentation

## Privacy

HAO keeps local preferences and search history on the device. Network lookups are sent only when the user starts a lookup. Official Aadhaar OTP entry is handled on UIDAI's own website; HAO does not request, read, or store the OTP.

Use test/synthetic identifiers when validating the app.

## Developer

**Chatpataprani**
