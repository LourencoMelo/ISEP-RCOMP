package testingFiles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        final File[] fileToSend = new File[1];

        JFrame jFrame = new JFrame("RCOMP-SPRINT4/5");
        jFrame.setSize(1000, 650);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jLabelTitulo = new JLabel("RCOMP-SPRINT4/5");
        jLabelTitulo.setFont(new Font("Arial", Font.BOLD, 50));
        jLabelTitulo.setBorder(new EmptyBorder(25, 5, 10, 5));
        jLabelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choose a file to send to Center of Distribuition");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 25));
        jlFileName.setBorder(new EmptyBorder(35, 0, 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(150, 5, 20 ,5));
        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(300, 150));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 40));
        JButton jbChooseFile = new JButton("Choose File");
        jbChooseFile.setPreferredSize(new Dimension(300, 150));
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 40));

        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);

        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send to Center of Distribuition");
                if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    jlFileName.setText("File selected to be sent:" + fileToSend[0].getName());
                }
            }
        });

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(fileToSend[0] == null){
                    jlFileName.setText("There wasn't any file choosen!");
                }else{
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 1234);
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        String fileName = fileToSend[0].getName();

                        byte[] fileNameInBytes = fileName.getBytes();
                        byte[] fileBytes = new byte[(int)fileToSend[0].length()];

                        fileInputStream.read(fileBytes);
                        dataOutputStream.writeInt(fileNameInBytes.length);
                        dataOutputStream.write(fileNameInBytes);
                        dataOutputStream.writeInt(fileBytes.length);
                        dataOutputStream.write(fileBytes);
                    }catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                }

            }
        });

        jFrame.add(jLabelTitulo);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);

    }
}
