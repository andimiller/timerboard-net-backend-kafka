# Architecture

## Streams

This application is modelled using streams and ktables with kafka.

@startuml

queue input
control transform
database tables {
  database systems
  database corps
  database alliances
}
queue output
cloud esi



esi -> input
input -> transform
transform -> output
esi -> tables
tables -> transform

@enduml