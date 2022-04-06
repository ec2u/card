---
title: Interact with the ESC Router
---

The Centralized Registry provides several functions, all of them provided as a REST Service. In a nutshell, the Registry holds student and card information and offers an easy way to verify card validity. See [Verify the Validity of a European Student Card](https://www.notion.so/Verify-the-Validity-of-a-European-Student-Card-a6bbb3899b2c41059415f7d987762ad4)

Before HEIs can interact with the Centralized Registry, they should register themselves on [https://router.europeanstudentcard.eu/](https://router.europeanstudentcard.eu/). After registration, an API key is released and will be used for authenticate HEIs over the REST services.

# URI

There are two different Centralized Registries for both production and test environments:

- Sandbox : **[https://api-sandbox.europeanstudentcard.eu/v1](https://api-sandbox.europeanstudentcard.eu/v1)** for integrations and qualifications tests.
- Production : **[https://api.europeanstudentcard.eu/v1](https://api.europeanstudentcard.eu/v1)** for real data only.

# REST APIs

The services provided by the Centralized Registry can be summarized as follows:

1. storing a new student
2. getting/updating/deleting an existing user
3. issuing a new card
4. getting all issued cards
5. verifying card validity
6. getting/updating/deleting an issued card

Further information can be found on [https://router.europeanstudentcard.eu/docs/api-doc](https://router.europeanstudentcard.eu/docs/api-doc)