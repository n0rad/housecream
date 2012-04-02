package net.awired.housecream.client.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.test.DefaultITDomainHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;

public class HccResourceIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_reset_data() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setDescription("new Description");
        hcc.getHccResource().updateDevice(deviceInfo);
        hcc.reset();

        HccDevice deviceInfo2 = hcc.getHccResource().getDeviceInfo();
        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo2, DefaultITDomainHelper.buildDefaultDevice()));
    }

    @Test
    public void should_get_root_resource() throws Exception {

        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();

        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice()));
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_technical_description() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setTechnicalDescription("new Description");

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test
    public void should_update_description() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setDescription("new Description");

        HccDevice updateDevice = hcc.getHccResource().updateDevice(deviceInfo);

        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(),
                "description"));
        assertEquals("new Description", updateDevice.getDescription());
        HccDevice device2 = hcc.getHccResource().getDeviceInfo();
        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(),
                "description"));
        assertEquals("new Description", device2.getDescription());
    }

    @Test
    public void should_update_name() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setName("new name");

        HccDevice updateDevice = hcc.getHccResource().updateDevice(deviceInfo);

        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(), "name"));
        assertEquals("new name", updateDevice.getName());
        HccDevice device2 = hcc.getHccResource().getDeviceInfo();
        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(), "name"));
        assertEquals("new name", device2.getName());
    }

    @Test
    public void should_update_notifyurl() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setNotifyUrl("http://localhost:5353");

        HccDevice updateDevice = hcc.getHccResource().updateDevice(deviceInfo);

        assertTrue(EqualsBuilder
                .reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(), "notifyUrl"));
        assertEquals("http://localhost:5353", updateDevice.getNotifyUrl());
        HccDevice device2 = hcc.getHccResource().getDeviceInfo();
        assertTrue(EqualsBuilder
                .reflectionEquals(deviceInfo, DefaultITDomainHelper.buildDefaultDevice(), "notifyUrl"));
        assertEquals("http://localhost:5353", device2.getNotifyUrl());
    }

    @Test
    public void should_be_able_to_update_all() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setNotifyUrl("http://localhost:5353");
        deviceInfo.setDescription("desc");
        deviceInfo.setName("GFDSQ");

        HccDevice updateDevice = hcc.getHccResource().updateDevice(deviceInfo);

        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, updateDevice));
        HccDevice device2 = hcc.getHccResource().getDeviceInfo();
        assertTrue(EqualsBuilder.reflectionEquals(deviceInfo, device2));
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_ip() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setIp("23456789");

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_port() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setPort(4242);

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_mac() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setMac("ZERTYU");

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_number_of_pin() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setNumberOfPin(42);

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_software() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setSoftware("23456789");

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_version() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setVersion("23456789");

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_name_to_null() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setName(null);

        hcc.getHccResource().updateDevice(deviceInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_description_to_null() throws Exception {
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setDescription(null);

        hcc.getHccResource().updateDevice(deviceInfo);
    }
}
