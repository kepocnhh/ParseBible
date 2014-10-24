package parsebibl;

public class ping_old extends BM_old
{
    String ping = "ping";
    public String GetPing()
    {
        return this.ping;
    }
    public ping_old(String message)
    {
        this.ping = message;
    }
    public ping_old(String message, String version)
    {
        this.ping = message;
        this.SetVersion(version);
    }
}