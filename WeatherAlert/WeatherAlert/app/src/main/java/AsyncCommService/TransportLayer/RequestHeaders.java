package AsyncCommService.TransportLayer;

/**
 * Created by Island on 17/03/16.
 */
public class RequestHeaders {


    public String m_cHeader;
    public String m_cValue;

    public RequestHeaders() {
        m_cHeader = m_cValue = null;
    }

    public RequestHeaders(String pHeader, String pValue) {
        m_cHeader = pHeader;
        m_cValue = pValue;
    }


    public void Dispose(){
        m_cHeader = m_cValue = null;
    }
}
