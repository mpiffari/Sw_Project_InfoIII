import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("ec578c9a-7852-46aa-b71c-3b143721662c")
public class DatabaseConnector implements QueryPerformer {
    @objid ("ce5325fe-2f1a-499b-91a8-400e8efe498c")
    private String RDS_INSTANCE_HOSTNAME;

    @objid ("bdc554e7-00f6-47a8-8618-3a852e2b7705")
    private int RDS_INSTANCE_PORT;

    @objid ("2ea1f5f1-6642-4e32-80f5-7800f3fad9b6")
    private String REGION_NAME;

    @objid ("29827b53-c3b6-4fba-8de7-8cee68459610")
    private String DB_USER;

    @objid ("3892257a-9fc9-4649-9280-854853e65417")
    private String JDBC_URL;

    @objid ("4c5991b5-0326-44c1-a9e4-15f83c9b0e3b")
    private DatabaseConnector() {
    }

    @objid ("688ae4cb-9f4a-4134-8fce-bb633963b725")
    public ResultSet executeQuery(String sql_query) {
    }

}
