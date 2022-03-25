# Accessibility

## Preamble

GTFS and Netex define accessibility primarily in terms of binary access for wheelchair users: it's
either on or off. Whilst it is the desire of the OTP developers to broaden the scope of
accessibility the lack of data limits us to use this definition in the implementation and in this
document.

## Evaluation types

Many agencies have the same problem: data on wheelchair-accessibility is, if it exists at all,
patchy. If you only included trips and stops that are explicitly set to be wheelchair-accessible
rather than unknown, it would be hard to get any result at all. For this reason OTP offers two
configurations for evaluation of the data:

- known information only: we want to only show results which are, according to the input data,
  wheelchair-accessible
- allow unknown information: in this mode we allow wheelchair users to use stops and trips which
  have unknown accessibility information (or - as a last resort - are known to be inaccessible), but
  they are selected only of no better alternatives are found

## Configuration

You can configure which type of evaluation type should be used in `router-config.json`:

```json
{
  "routingDefaults": {},
  "accessibility": {
    "evaluation": "ALLOW_UNKNOWN_INFORMATION"
  },
  "updaters": []
}

```

The possible values of `evaluation` are:

- `KNOWN_INFORMATION_ONLY` (default)
- `ALLOW_UNKNOWN_INFORMATION`

## Accessible transfers

By default OTP only pre-calculates transfers between stops for able-bodied walkers. If they have
no obstacles wheelchair users can use them, too, but there won't be guaranteed to be one.

If you want OTP to also pre-generate wheelchair-accessible transfers use the following configuration
in `build-settings.json`:

```json
{
  "transferRequests": [ { "modes": "WALK" }, { "modes": "WALK", "wheelchair": true } ]
}
```

This results in OTP calculating an accessible transfer if the first one is found to be inaccessible
to wheelchair users.