import java.io.*;
import java.net.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;

public class launch extends javax.swing.JFrame
{
    static String logo_address = "C:\\Users\\Parth Prajapati\\Desktop\\VS_JAVA\\logo3.png";
    static String username = "", last_msgs = "";
    static int S_or_C;
	String IP_address;
    JTextField message = new JTextField();
    JButton send = new JButton();
    JTextArea chat = new JTextArea();
    ActionListener al;
    boolean ready = false;
    launch pt;
    boolean HOST_OR_JOIN;
    String CLI_OR_SER;
    ServerSocket ss;
    Socket s;
    BufferedReader br;
    PrintWriter pout;
    JMenuBar menu_bar;
    static Color colors = new Color(255, 255, 255);

    public launch(boolean host_or_join, String IP_add)
    {
        // menu bar setup
        menu_bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu color = new JMenu("Color");
        menu_bar.add(file);
        menu_bar.add(color);
        JMenuItem quit = new JMenuItem("Quit");
        JMenuItem erase = new JMenuItem("Erase");
        JMenuItem restore = new JMenuItem("Restore");
        JMenuItem font = new JMenuItem("Font");
        JMenuItem background = new JMenuItem("Background");
        file.add(quit);
        file.add(erase);
        file.add(restore);
        color.add(font);
        color.add(background);
        setJMenuBar(menu_bar);

        IP_address = IP_add;
        
        // frame look set up with all components
        ImageIcon logo = new ImageIcon(logo_address);
        Image image = logo.getImage(); 
        setIconImage(image);
        setTitle("NPsquare ChatBox");
        setLayout(null);
        setSize(515, 570);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(message);
        message.setLocation(5, 465);
        message.setSize(420, 30);
        message.setEnabled(false);
		message.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(send);
        send.setLocation(430, 465);
        send.setText("Send");
        send.setSize(65, 30);
        add(chat);
        chat.setEditable(false);
        chat.setBorder(new EtchedBorder());
        chat.setLocation(5, 5);
        chat.setSize(490, 455); 
		chat.setBackground(Color.black);
        
        // action listeners for all the events
        quit.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                ImageIcon logo = new ImageIcon(logo_address);
                final Image image = logo.getImage(); 
                final Image new_image = image.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH);  
                logo = new ImageIcon(new_image);
                int quit = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "NPsquare ChatBox", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, logo);
                if(quit == 0)
                    System.exit(0);
            }  
        });
        erase.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                last_msgs += chat.getText();
                chat.setText("");
            }  
        });
        restore.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                String temp = chat.getText();
                chat.setText(last_msgs + temp);
                last_msgs = "";
            }  
        });
        font.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                colors = JColorChooser.showDialog(launch.this,"Choose a color", chat.getForeground());
                if (colors != null)
                    chat.setForeground(colors);
            }  
        });
        background.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                Color color = JColorChooser.showDialog(launch.this,"Choose a color", chat.getBackground());
                if (color != null)
                    chat.setBackground(color);
            }  
        });
        
        al = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ready = true;
            }
        };
        send.addActionListener(al);
        pt = this;
        HOST_OR_JOIN = host_or_join;
        messenger.start();
    }

    public static void server_or_client()
    {
        Object[] options = {"Host", "Join"};
        ImageIcon logo = new ImageIcon(logo_address);
        final Image image = logo.getImage(); 
        final Image new_image = image.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH);  
        logo = new ImageIcon(new_image);
        while(username.equals(""))
        {
            username = (String)JOptionPane.showInputDialog(null, "Enter your username", "NPsquare ChatBox", JOptionPane.INFORMATION_MESSAGE, logo, null,"");      
            if(username==null)
				System.exit(0);
			else if(username.equals(""))
            {
				JOptionPane.showMessageDialog(null,"Please enter the user name!","Alert",JOptionPane.WARNING_MESSAGE); 
				
                
            }    
        }
        S_or_C = JOptionPane.showOptionDialog(null, "Host a chat or Join a chat?", "Welcome to NPsquare ChatBox", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, logo, options, options[0]);
    }
    public static void main(String[] args)
    {
        server_or_client();
        if(S_or_C == 0)
        {
            new launch(true, null).setVisible(true);
        }
        else
        {
            ImageIcon logo = new ImageIcon(logo_address);
            final Image image = logo.getImage(); 
            final Image new_image = image.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH);  
            logo = new ImageIcon(new_image);
            String IP_address = "";
            while(IP_address.equals(""))
            {
                IP_address = (String)JOptionPane.showInputDialog(null, "Enter the IP address", "NPsquare ChatBox", JOptionPane.INFORMATION_MESSAGE, logo, null, "");
                if(IP_address == null)
                {
                    System.exit(0);
                } 
            } 
            try
            {
                InetAddress.getByName(IP_address);
                new launch(false, IP_address).setVisible(true);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "Invalid or Unreachable IP");
            }
        }
    }    

    Thread messenger = new Thread()
    {
        public void run()
        {
            try
            {
                if(HOST_OR_JOIN)
                {
					
                    chat.setText("Waiting for an incoming connection.\nEnter my IP at client side.\nMy IP: " + InetAddress.getLocalHost().getHostAddress()); 
					chat.setFont(new Font("Arial", Font.BOLD, 14));
					chat.setForeground(Color.white);
                    ss = new ServerSocket(9999);
                    s = ss.accept();
                    s.setKeepAlive(true);
                }
                else
                {
                    chat.setText("Connecting to:" + IP_address + ":9999"); 
					chat.setFont(new Font("Arial", Font.BOLD, 14));
					chat.setForeground(Color.white);
                    s = new Socket(InetAddress.getByName(IP_address),9999);
                }
                message.setEnabled(true);
                pout = new PrintWriter(s.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                chat.setText(chat.getText() + "\nConnected to:" + s.getInetAddress().getHostAddress() + ":" + s.getPort()+"\n"+"                                           "+java.time.LocalDate.now());
                
				chat.setForeground(Color.white);
				while(true)
                {
                    if(ready)
                    {
                        String test=message.getText();
                        pout.println(username+": "+test);
						Calendar cal = Calendar.getInstance();
						Date date=cal.getTime();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						String formattedDate=dateFormat.format(date);
					    chat.setText(chat.getText()+"\n"+"["+formattedDate+"] "+username+": "+test);
						chat.setForeground(colors);
                        message.setText("");
                        ready = false;
                    }
                    if(br.ready())
                    {
                        String test=br.readLine();
						Calendar cal = Calendar.getInstance();
						Date date=cal.getTime();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						String formattedDate=dateFormat.format(date);
                        chat.setText(chat.getText()+"\n"+"["+formattedDate+"] "+test);
						chat.setForeground(colors);
                    }   
                    Thread.sleep(80);
                }                
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(pt, ex.getMessage());
                chat.setText("Cannot connect!");
				chat.setForeground(colors);
                try
                {
                    wait(3000);
                }
                catch(InterruptedException ex1) {}
                System.exit(0);
            }
        }
    };
}