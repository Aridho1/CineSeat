# ANCHOR

##PPT
https://docs.google.com/presentation/d/1Jg_er2jlb8mwoxM35UKZeYKDXC8d-jyy/edit?usp=sharing&ouid=117750888484878399277&rtpof=true&sd=true

## FLOWCHART
### DEV
flowchart LR

A[Start Application] --> B[Select Movie]
B --> C[System Load Seats for Selected Movie]

C --> D[Display Seat Layout]

D --> E[User Select / Unselect Seats]

E --> F{Action?}

F -->|Change Movie| G[Save Selected Seats per Movie]
G --> B

F -->|Book Ticket| H[Validate Customer Name]
H --> I{Name Valid?}

I -->|No| J[Show Error Message]
J --> D

I -->|Yes| K[Filter Selected Seats]
%% K --> L{Seats Selected?}

%% L -->|No| M[Show Warning Message]
%% M --> D
K --> N
%% L -->|Yes| 
N[Calculate Ticket Price]

N --> O[For Each Seat]
O --> P[Movie Price + Seat Extra via Polymorphism]
P --> Q[Sum Total]

Q --> R[Generate Receipt]

R --> S[Display Receipt]

S --> T[End / Stay in App]


### USER
flowchart LR

A[Start Application] --> B[Enter Customer Name]

B --> C[Select Movie]

C --> D[View Seat Layout]

D --> E[Select Seat(s)]

E --> F{Booking Ready?}

F -->|No| G[Modify Selection]
G --> D

F -->|Yes| H[Confirm Booking]

H --> I{Customer Data Complete?}

I -->|No| J[Show Error Message]
J --> B

I -->|Yes| K[Process Ticket]

K --> L[Display Receipt]

L --> M[Finish]


### Mermaid
https://mermaid.live/edit
