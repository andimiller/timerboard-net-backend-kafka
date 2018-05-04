# Architecture

## Streams

This application is modelled using streams and ktables with kafka.

@startuml

database esi

queue sov
queue alliance
queue system

poller -> esi : poll
esi --> sov : response
sov -> augmenter : trigger augmentations
augmenter -> esi : ask for extra hydration data
esi --> alliance : alliance data
esi --> system : system data

sov --> augmenter
alliance --> augmenter
system --> augmenter

queue hydrated_sov

augmenter -> hydrated_sov

hydrated_sov -> egress : collect

egress -> user : websocket fanout

@enduml