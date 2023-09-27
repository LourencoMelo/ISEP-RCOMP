RCOMP 2020-2021 Project - Sprint 3 planning
===========================================
### Sprint master: 1190782 ###

# 1. Sprint's backlog #

In this sprint, each team member will keep working on the same network simulation from the previous 
sprint (regarding the same building). From the already established layer three configurations, now OSPF 
based dynamic routing will be used to replace static routing used previously.

# 2. Technical decisions and coordination #

  |  |  |
  |:-------------:| :-------------: |
  |VoIP model | 7960 |
  | HTTP server name | serverP |

  |**Building 1**|  |
  |:-------------:| :-------------: |
  |OSPF area id | 1 |
  | Ecompasses all IPv4 networks | |
  | OSPF routing protocol will have the default route pointing to the ISP router |  |
  | DNS root domain name | rcomp-20-21-de-g1 |
  | IPv4 address | |
  | DNS name server | ns.rcomp-20-21-de-g1 |
  | VoIP phone prefix | 1 |

  |**Building 2**|  |
  |:-------------:| :-------------: |
  |OSPF area id | 2 |
  | Ecompasses all IPv4 networks | |
  | OSPF routing protocol will have the default route pointing to the ISP router |  |
  | DNS root domain name | building-2.rcomp-20-21-de-g1 |
  | DNS name server | ns.building-2.rcomp-20-21-de-g1 |
  | VoIP phone prefix | 2 |
  
  |**Building 3**|  |
  |:-------------:| :-------------: |
  |OSPF area id | 3 |
  | Ecompasses all IPv4 networks | |
  | OSPF routing protocol will have the default route pointing to the ISP router |  |
  | DNS root domain name | building-3.rcomp-20-21-de-g1 |
  | IPv4 address | |
  | DNS name server | ns.building-3.rcomp-20-21-de-g1 |
  | VoIP phone prefix | 3 |
  
  |**Building 4**|  |
  |:-------------:| :-------------: |
  |OSPF area id | 4 |
  | Ecompasses all IPv4 networks | |
  | OSPF routing protocol will have the default route pointing to the ISP router |  |
  | DNS root domain name | building-4.rcomp-20-21-de-g1 |
  | IPv4 address | |
  | DNS name server | ns.building-4.rcomp-20-21-de-g1 |
  | VoIP phone prefix | 4 |

* Backbone OSPF area id = 0 - Only ecompasses one IPv4 network which is the backbone VLAN

# 3. Subtasks assignment #
* 1190782 - Update the campus.pkt layer three Packet Tracer simulation from the previous sprint, to include the described features in this sprint for building 1.
* 1190811 - Update the campus.pkt layer three Packet Tracer simulation from the previous sprint, to include the described features in this sprint for building 2. Final integration of each member’s Packet Tracer simulation into a single simulation.
* 1190718 - Update the campus.pkt layer three Packet Tracer simulation from the previous sprint, to include the described features in this sprint for building 3.
* 1191419 - Update the campus.pkt layer three Packet Tracer simulation from the previous sprint, to include the described features in this sprint for building 4. 
