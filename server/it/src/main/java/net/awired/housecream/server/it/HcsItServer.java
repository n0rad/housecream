package net.awired.housecream.server.it;

import net.awired.ajsl.test.LoggingRule;
import net.awired.housecream.server.common.resource.HcRestMcuNotifyResource;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.common.resource.InPointsResource;
import net.awired.housecream.server.common.resource.OutPointResource;
import net.awired.housecream.server.common.resource.OutPointsResource;
import net.awired.housecream.server.common.resource.RuleResource;
import net.awired.housecream.server.common.resource.RulesResource;
import net.awired.housecream.server.common.resource.ZoneResource;
import net.awired.housecream.server.common.resource.ZonesResource;

public class HcsItServer extends LoggingRule {

    private HcsItContext context = new HcsItContext();

    public InPointResource inPointResource() {
        return context.buildResourceProxy(InPointResource.class, new HcsItSession());
    }

    public InPointsResource inPointsResource() {
        return context.buildResourceProxy(InPointsResource.class, new HcsItSession());
    }

    public OutPointsResource outPointsResource() {
        return context.buildResourceProxy(OutPointsResource.class, new HcsItSession());
    }

    public OutPointResource outPointResource() {
        return context.buildResourceProxy(OutPointResource.class, new HcsItSession());
    }

    public RulesResource rulesResource() {
        return context.buildResourceProxy(RulesResource.class, new HcsItSession());
    }

    public RuleResource ruleResource() {
        HcsItSession session = new HcsItSession();
        session.setJson(true);
        return context.buildResourceProxy(RuleResource.class, session);
    }

    public ZoneResource zoneResource() {
        return context.buildResourceProxy(ZoneResource.class, new HcsItSession());
    }

    public ZonesResource zonesResource() {
        return context.buildResourceProxy(ZonesResource.class, new HcsItSession());
    }

    public HcRestMcuNotifyResource notifyResource() {
        return context.buildResourceProxy(HcRestMcuNotifyResource.class, new HcsItSession());
    }

    @Override
    public void before() throws Throwable {
        inPointsResource().deleteAllInPoints();
        outPointsResource().deleteAllOutPoints();
        rulesResource().deleteAllRules();
        zonesResource().deleteAllZones();
    }

    @Override
    protected void after() {
    }

}
