package si.fri.tabletop.customers.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    /*
     * If we will need this, uncomment and adjust for specific service.
     */
    /*
    @ConfigValue(value = "external-services.menu-service.enabled", watch = true)
    private boolean menuServiceEnabled;

    public boolean isMenuServiceEnabled() {
        return menuServiceEnabled;
    }

    public void setMenuServiceEnabled(boolean menuServiceEnabled) {
        this.menuServiceEnabled = menuServiceEnabled;
    }
    */
}
