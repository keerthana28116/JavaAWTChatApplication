package chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class momo extends Frame implements Runnable, ActionListener {

    TextField tf;
    Button b;
    TextArea ta;
    Thread chat;
    DataInputStream datain;
    DataOutputStream dataout;
    Socket client;

    momo() {
   
        tf = new TextField();
        tf.setBounds(100, 50, 160, 50);

        ta = new TextArea();
        ta.setBounds(50, 150, 330, 200);

        b = new Button("Send");
        b.setBounds(150, 400, 50, 30);

        add(tf);
        add(ta);
        add(b);

        b.addActionListener(this);

        setTitle("momo (Client)");
        setSize(500, 500);
        setLayout(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try { if (client != null) client.close(); } catch (Exception ex) {}
                System.exit(0);
            }
        });

        new Thread(() -> {
            try {
                client = new Socket("localhost", 12345);
                datain = new DataInputStream(client.getInputStream());
                dataout = new DataOutputStream(client.getOutputStream());

                chat = new Thread(this);
                chat.setDaemon(true);
                chat.start();

            } catch (Exception e) {
                System.out.println("Client error: " + e);
            }
        }).start();
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String msg = tf.getText();
            ta.append("momo: " + msg + "\n");
         
                dataout.writeUTF(msg);
                dataout.flush();
            
            tf.setText("");
        } catch (Exception ex) {
            System.out.println("Send Error: " + ex);
        }
    }

    public void run() {
        while (true) {
            try {
                    String msg = datain.readUTF();
                    ta.append("kiki: " + msg + "\n");
                
            } catch (Exception e) {
                System.out.println("Receive Error: " + e);
                break;
            }
        }
    }

    public static void main(String[] args) {
        new momo();
    }
}
