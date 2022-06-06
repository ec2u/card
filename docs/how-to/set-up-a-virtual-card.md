# Review eduGAIN Profile

- required attributes
  - cn
  - mail
  - schacPersonalUniqueCode
  - schacHomeOrganisation

# …

## Review Legal Matters

- about
- privacy
  - template @ https://wiki.refeds.org/display/CODE/Privacy+policy+guidelines+for+Service+Providers
- contacts

## Configure Tenants

```json
{
    "example.edu": {
        "hei": {
            "pic": 12345678,
            "schac": "example.edu",
            "name": "Example University",
            "home": "https://www.example.edu/",
            "logo": "https://www.example.edu/assets/logo.svg",
            "iso": "EU",
            "country": "Europe"
        },
        "esc": {
            "api": "https://sandbox.europeanstudentcard.eu/v1/",
            "tst": "http://esc.gg/",
            "key": "############################"
        }
    }
}
```

## Deploy

# Register with eduGAIN

- as service provider