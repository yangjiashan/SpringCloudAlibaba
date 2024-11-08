package org.yang.study;

import org.ietf.jgss.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Base64;
import java.util.Set;

public class KerberosAuthenticationExample {

    private static String strKrb5MechOid = "1.2.840.113554.1.2.2";
    private static String strSpnegoOid = "1.3.6.1.5.5.2";

    private static String strPrincipalClient = "bigdata/node179@LINEWELL.COM";
    private static String strPrincipalServer = "bigdata/node79@LINEWELL.COM";

    public static void main(String[] args) throws GSSException, IOException {
        LoginContext lc = null;
        try {
            System.setProperty("java.security.auth.login.config", "D:\\ideaspacesss\\data-monitor\\data-monitor-admin\\src\\main\\resources\\jaas.conf");
            System.setProperty("java.security.krb5.conf", "D:\\ideaspacesss\\data-monitor\\data-monitor-admin\\src\\main\\resources\\krb5.conf");
            lc = new LoginContext("KerberosLogin", new KerberosCallbackHandler());
            // 使用Keytab和Principal登录
            lc.login();
            // 登录成功后，可以从Subject中获取用户身份和凭证
            Subject subject = lc.getSubject();
            Set<Principal> principals = subject.getPrincipals();

            System.out.println(principals);
            System.out.println("User authenticated successfully.");


            System.out.println("Subject.doAs() ... ");
            GSSContext gssContext = Subject.doAs(subject, new PrivilegedExceptionAction<GSSContext>() {

                final Oid krb5MechOid = new Oid(strKrb5MechOid);  // Kerberos authentication
                final Oid spnegoOid = new Oid(strSpnegoOid);      // SPNEGO authentication

                @Override
                public GSSContext run() throws Exception {

                    GSSManager manager = GSSManager.getInstance();

                    //
                    GSSName gssClientName = manager.createName(strPrincipalClient, GSSName.NT_USER_NAME);
                    GSSCredential clientGssCreds = manager.createCredential(gssClientName,
                            GSSCredential.INDEFINITE_LIFETIME,
                            krb5MechOid,
                            GSSCredential.INITIATE_ONLY);

                    // GSS ticket or token
                    GSSName gssServerName = manager.createName(strPrincipalServer, GSSName.NT_USER_NAME);
                    GSSContext context = manager.createContext(gssServerName,
                            null,  // spnegoOid, krb5MechOid or null (null equals Kerberos authentication)
                            clientGssCreds,
                            GSSContext.DEFAULT_LIFETIME);

                    return context;
                }
            });

            if (gssContext == null) {
                System.out.println("gssContext == null");
                return;
            }

            System.out.println(gssContext);


            gssContext.requestCredDeleg(true);
            gssContext.requestMutualAuth(true);  // Mutual authentication
            gssContext.requestConf(true);        // Will use confidentiality later
            gssContext.requestInteg(true);       // Will use integrity later

            // Connect to server
            Socket clientSocket = new Socket("10.231.6.79", 9083);
            DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Connected to server: " + clientSocket.getInetAddress());

            // Do the context loop
            byte[] token = new byte[0];
            while (!gssContext.isEstablished()) {

                token = gssContext.initSecContext(token, 0, token.length);

                // Send a token to the server if one was generated by
                // initSecContext
                if (token != null) {

                    byte[] encodedToken = Base64.getEncoder().encode(token);
                    System.out.println("outStream.writeInt(): encodedToken.length == " + encodedToken.length);

                    outStream.writeInt(encodedToken.length);
                    outStream.write(encodedToken);
                    outStream.flush();
                }

                // If the client is done with context establishment
                // then there will be no more tokens to read in this loop
                if (!gssContext.isEstablished()) {
                    token = new byte[inStream.readInt()];
                    System.out.println("inStream.writeInt(): token.length == " + token.length);
                    inStream.readFully(token);
                }
            }

            System.out.println("gssContext.isEstablished() == " + gssContext.isEstablished());
            System.out.println("client: " + gssContext.getSrcName());
            System.out.println("server: " + gssContext.getTargName());

            if (gssContext.getMutualAuthState())
                System.out.println("Mutual authentication is enable!");

        } catch (LoginException le) {
            le.printStackTrace();
            System.err.println("Failed to login using Kerberos.");
        } catch (PrivilegedActionException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭LoginContext以释放资源
            if (lc != null) {
                try {
                    lc.logout();
                } catch (LoginException le) {
                    System.err.println("Error during logout: " + le.getMessage());
                }
            }
        }
    }

    static class KerberosCallbackHandler implements CallbackHandler {
        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    // 设置principal名称
                    ((NameCallback) callback).setName("bigdata/node179@LINEWELL.COM");
                } else if (callback instanceof PasswordCallback) {
                    // Keytab文件中的密码通常为空，但在此处设置
                    ((PasswordCallback) callback).setPassword(new char[0]);
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }
}
