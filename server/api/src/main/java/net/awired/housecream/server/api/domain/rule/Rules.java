package net.awired.housecream.server.api.domain.rule;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "rules")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rules {

    @XmlElement(name = "rule")
    private List<EventRule> rules;
    private Long total;

    public Rules() {
    }

    public Rules(List<EventRule> rules, Long total) {
        this.rules = rules;
        this.total = total;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("total", total) //
                .add("rules", rules) //
                .toString();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<EventRule> getRules() {
        return rules;
    }

    public void setRules(List<EventRule> rules) {
        this.rules = rules;
    }
}
