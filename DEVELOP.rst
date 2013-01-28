
http://www.slideshare.net/ge0ffrey/20110329-london-drools


land > building > floor > room > area
land > area

todo in jetty :
- SSL
- GZip

in
==
restmcu // event
timer // event
snmp // event
web // command
xmpp // command
x10 // event
knx // event
ssh // command

out
===
restmcu // event
mail // notif
sip // notif 
sms // notif
xmpp // notif
x10 // event
knx // event
log // notif
nagios // notif
snmp // notif
DB // notif
ssh ? 
file ?






rule "my first rule"
    when
Event(pointId == 368, value == 1.0)
not ConsequenceAction(outPointId == (long)365)
not ConsequenceAction(outPointId == (long)365)
    then
insert(new ConsequenceAction((long)365,(float)1.0, 0, null));
insert(new ConsequenceAction((long)365,(float)0.0, 5000, TriggerType.NON_RETRIGGER));
end








rule "my first rule"
    when
Event(pointId == 369, value == 1.0)
not ConsequenceAction(outPointId == (long)366)
not ConsequenceAction(outPointId == (long)366)
    then
insert(new ConsequenceAction((long)366,(float)1.0, 0, null));
insert(new ConsequenceAction((long)366,(float)0.0, 5000, TriggerType.RETRIGGER));
end

rule "my first rule-RETRIGGER369"
salience 10
    when
Event(pointId == 369, value == 1.0)
$retrigger:Action(outPointId == (long)366)
    then
retract($retrigger);
end
















