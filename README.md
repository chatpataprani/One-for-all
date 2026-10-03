# HAO

A learning-focused **OSINT workspace** by **chatpataprani**.

## App direction
- App name: **HAO**
- Full uploaded square logo is bundled as `hao-logo.svg` with all four corners preserved.
- OSINT-style modules stay inside the HAO workspace.
- No external tool-page redirects or iframes.
- Responsive dark UI with app-style navigation and mobile bottom navigation.

## Modules
Username Search, Email OSINT, Phone OSINT, IP Intelligence, Domain Intelligence, URL Scanner, Image Metadata, Document Metadata, Hash Analyzer, IMEI Check, Aadhaar Test Lookup, IFSC Finder, Vehicle Info, Stego Detector and QR Analyzer.

## Both-db test lab
The supplied fake/test Both-db API remains available inside **DB Lab**:
- Number: `https://both-db.vercel.app/number=`
- Aadhaar: `https://both-db.vercel.app/aadhar=`

Use synthetic/test identifiers while learning. If the browser reports a CORS error, the Both-db deployment must allow requests from the HAO origin.

## Build
Reproducible static/PWA build via `npm run build` and GitHub Actions. The build includes the HAO logo asset.
