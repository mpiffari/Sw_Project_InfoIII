import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e529e104-c3fb-4f3a-870c-0393c604c0a0")
public interface AndroidCommunicator {
    @objid ("0108d5b4-528d-481c-af88-8674c71c5be5")
    String getRDS_INSTANCE_HOSTNAME();

    @objid ("8f9e94d8-3a4e-4d75-9505-18b3d58a3fd8")
    void setRDS_INSTANCE_HOSTNAME(String value);

    @objid ("be7ca4bb-fdd9-4e8c-b428-e5988b30cc82")
    int getRDS_INSTANCE_PORT();

    @objid ("f352befd-9cd1-49e4-a36d-695ec5a6844f")
    void setRDS_INSTANCE_PORT(int value);

    @objid ("a69ec204-fe6f-481a-a214-64ebf4a344c5")
    String getREGION_NAME();

    @objid ("7f6b3549-9618-4790-9894-198543ae0990")
    void setREGION_NAME(String value);

    @objid ("d07e8612-648c-4299-9bd4-dc536ec25d88")
    String getDB_USER();

    @objid ("710c0f43-80e3-4383-887b-1cea380e3d76")
    void setDB_USER(String value);

    @objid ("10e987ba-18c4-488f-92b8-9d92bee89549")
    String getJDBC_URL();

    @objid ("33f8693c-9bc2-4909-8289-ce108cf675c8")
    void setJDBC_URL(String value);

}
