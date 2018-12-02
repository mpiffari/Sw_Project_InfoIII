import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("76e73206-f0a5-48da-b6b9-0d62c903e877")
public interface QueryPerformer {
    @objid ("5c01aa00-74c2-4b1a-82ba-1a26abfaf75f")
    ResultSet executeQuery(String sql_query);

}
