# Architecture

## Streams

This application is modelled using streams and ktables with kafka.

@startuml

database esi

box "Input kafka topics" #LightBlue
queue sov_in
end box

box "Request kafka topics" #Yellow
queue alliance_req
queue system_req
end box


control poller
control augmenter

control worker
box "Working data kafka tables" #Grey
queue alliance
queue system
end box

control combiner

box "Result kafka topics" #Green
queue sov_out
end box


control egress

actor user


loop polling
poller -> esi: got new sov data?
esi --> sov_in : response
end

loop augmenting
sov_in -> augmenter : trigger augmentations
augmenter -> alliance_req: ask for alliance data
augmenter -> system_req: ask for system data 
end

loop augmentation_requester
worker <- alliance_req
worker <- system_req
worker -> esi: give me this data
esi --> alliance
esi --> system
end

loop combining
sov_in -> combiner
alliance --> combiner
system --> combiner
combiner -> sov_out
end

loop egress
sov_out -> egress
egress -> user: websocket fanout
end

@enduml