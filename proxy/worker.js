export default {
  async fetch(request, env) {
    const url = new URL(request.url);

    if (request.method === "OPTIONS") {
      return new Response(null, {
        status: 204,
        headers: corsHeaders()
      });
    }

    if (url.pathname === "/health") {
      return json({ ok: true, service: "HAO proxy" }, 200);
    }

    if (url.pathname !== "/lookup" || request.method !== "GET") {
      return json({ error: "not_found" }, 404);
    }

    const type = url.searchParams.get("type");
    const value = url.searchParams.get("value")?.trim();

    if (!["number", "aadhar"].includes(type) || !value) {
      return json({ error: "invalid_request" }, 400);
    }

    const base = String(env.BOTH_DB_API_URL || "").replace(/\/$/, "");
    const path = type === "number"
      ? String(env.BOTH_DB_NUMBER_PATH || "")
      : String(env.BOTH_DB_AADHAAR_PATH || "");

    if (!base || !path) {
      return json({ error: "server_not_configured" }, 500);
    }

    const upstreamUrl = base + path + encodeURIComponent(value);
    const headers = { "Accept": "application/json" };

    if (env.BOTH_DB_API_KEY) {
      headers.Authorization = "Bearer " + env.BOTH_DB_API_KEY;
    }

    try {
      const upstream = await fetch(upstreamUrl, {
        method: "GET",
        headers
      });

      const body = await upstream.text();

      // Do not forward upstream server headers or its URL.
      return new Response(body, {
        status: upstream.status,
        headers: {
          ...corsHeaders(),
          "Content-Type": upstream.headers.get("content-type") || "application/json; charset=utf-8",
          "Cache-Control": "no-store"
        }
      });
    } catch {
      return json({ error: "upstream_unavailable" }, 502);
    }
  }
};

function corsHeaders() {
  return {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type, Authorization"
  };
}

function json(value, status) {
  return new Response(JSON.stringify(value), {
    status,
    headers: {
      ...corsHeaders(),
      "Content-Type": "application/json; charset=utf-8",
      "Cache-Control": "no-store"
    }
  });
}
