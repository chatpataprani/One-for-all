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

The supplied ZIP contains **57 HTML screens and 17 JavaScript files**. The current GitHub repository surfaces 26 tool entries from that source set; the full 17 MB extracted bundle is not currently committed into this repository.

The app does **not** open an external Punjab deployment or redirect users to another tool site.

## Run locally

```bash
python3 -m http.server 8080
```

Open the local server in a browser and use the app normally.
