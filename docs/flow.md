```mermaid
sequenceDiagram
  
  participant Browser
  participant Server
  participant Gate
  participant SAML
  participant Database
  participant Vault
  
  rect rgba(0,0,0,0.1)
  Browser ->> Server : GET /
  Server ->> Browser: 401 Unauthorized<br/>WWW-Authenticate: Bearer realm="…"<br/> Location: /gate/token
  
  Browser ->> Gate: GET  /gate/token
  Gate ->> Browser: 302 Found<br/> Location: /saml/…
  
  Browser ->> SAML : …
  SAML ->> Browser : 302 Found<br/> Location: /gate/token
  
  Gate ->> Browser : 200 OK<br/>

end

```