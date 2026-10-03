# One for All

A learning-focused all-in-one tools hub by **chatpataprani**.

## Included
- Responsive dark workflow inspired by focused utility apps.
- 26 tool entries surfaced from the supplied Punjab ZIP.
- Native in-app tool workspace.
- DB Lab for the supplied fake/test Both-db API.
- Number lookup: `https://both-db.vercel.app/number=`
- Aadhaar lookup: `https://both-db.vercel.app/aadhar=`

## Both-db test lab

The app renders the returned JSON as readable match cards and keeps the raw response available in the UI. Use synthetic/test identifiers while learning.

If the browser reports a CORS error, the Both-db deployment must allow requests from the One-for-all origin.

## Source ZIP status

The supplied ZIP contains **57 HTML screens and 17 JavaScript files**. The app keeps those 57 source-screen routes inside the One-for-all UI through an internal screen library. Selecting a tool or source screen stays in the app; there are no external page redirects or iframe navigations.

The original 17 MB extracted source bundle is not copied byte-for-byte into Git history; the app uses its internal route/workspace layer instead.

## Run locally

```bash
python3 -m http.server 8080
```

Open the local server in a browser and use the app normally.
