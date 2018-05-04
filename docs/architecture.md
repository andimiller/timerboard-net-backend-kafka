# Architecture

## Streams

This application is modelled using streams and ktables with kafka.

@startuml

database esi

queue sov_in
queue alliance_req
queue alliance
queue system_req
queue system
queue sov_out

control poller
control augmenter
control augmenter_poller
control combiner
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
augmenter_poller <- alliance_req
augmenter_poller <- system_req
augmenter_poller -> esi: give me this data
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