package kollus.jwt.gui;

import kollus.jwt.JwtEncoder;
import kollus.jwt.MediaItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;
import com.mysql.cj.jdbc.Driver;

public class guiFrame extends JFrame{
    private JButton run;
    private JTextField secretKeyText;
    private JTextField customKeyText;
    private JPanel calculatorView;
    private JProgressBar progressBar1;
    private JTextField channelKeyText;
    private JTextField customID;
    private JButton fileSelectButton;
    private boolean stateValue = true;
    private boolean stopValue = true;
    private SwingWorker sw1;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public guiFrame() {
        //fileSelectButton.addActionListener(new FileSearchView());
        run.addActionListener(new RunPurgeProgram());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Purge Cache Program") {
            @Override
            public void paint(Graphics g) {
                Dimension d = getSize();
                Dimension m = getMaximumSize();
                boolean resize = d.width > 700 || d.height > 300;
                d.width = 650;
                d.height = 250;
                if (resize) {
                    Point p = getLocation();
                    setVisible(false);
                    setSize(d);
                    setLocation(p);
                    setVisible(true);
                }
                super.paint(g);
            }
        };
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        frame.setPreferredSize(new Dimension(650, 250));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new guiFrame().calculatorView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
/*
    private class FileSearchView implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

           *//* JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("텍스트 파일 찾기");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File(.txt)", "txt");
            chooser.setFileFilter(filter);

            int ret = chooser.showOpenDialog(null);
            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String filePath = chooser.getSelectedFile().getPath();
            System.out.println(filePath);

            channelKeyText.setText(filePath);*//*
        }
    }*/

    private class RunPurgeProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(!run.getText().equals("RUN")){

                int result = JOptionPane.showConfirmDialog(null,
                        "purge를 강제 종료하시겠습니까?", "경고!!!",
                        JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    System.out.println("STOP!!!!!!!!!!!!!!!!!");
                    stopValue = false;
                    stateValue = false;
                    sw1.cancel(true);
                    JOptionPane.showMessageDialog(null, "Purge 작업을 종료했습니다.");
                    return;
                }
                return;
            }

            int result = JOptionPane.showConfirmDialog(null,
                    "purge를 진행하시겠습니까?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.CLOSED_OPTION){
                return;
            }else if(result != JOptionPane.YES_OPTION){
                return;
            }
            startThread();
        }
    }

    private void startThread()
    {

        sw1 = new SwingWorker()
        {

            @Override
            protected String doInBackground() throws Exception
            {
                String host = "https://v.kr.kollus.com";
                String cuid = "purge_sys_user";
                String channelKey = channelKeyText.getText();
                channelKey = channelKey.replaceAll(" ", "");
                String cus_ID = customID.getText();
                cus_ID = cus_ID.replaceAll(" ", "");
                String userKey = customKeyText.getText();
                userKey = userKey.replaceAll(" ", "");
                String secretKey = secretKeyText.getText();
                secretKey = secretKey.replaceAll(" ", "");

                if(cus_ID.length() == 0 || cus_ID == null){
                    JOptionPane.showMessageDialog(null, "customID를 입력해주세요!");
                    stateValue = false;
                    return "";
                }
                if(userKey.length() == 0 || userKey == null){
                    JOptionPane.showMessageDialog(null, "CustomKey를 입력해주세요!");
                    stateValue = false;
                    return "";
                }
                if(secretKey.length() == 0 || secretKey == null){
                    JOptionPane.showMessageDialog(null, "SecretKey를 입력해주세요!");
                    stateValue = false;
                    return "";
                }

                String sql;
                sql = "select mck.key from media_contents mc inner join media_content_keys mck on mc.id = mck.media_content_id where mc.content_provider_id = ";
                sql+=cus_ID;
                if(channelKey.length() > 0 && channelKey != null){
                    sql+=" and media_package_key= '"+channelKey+"'";
                }
                System.out.println(channelKey.length());
                System.out.println(sql);
                try{

                    String driverName = "com.mysql.cj.jdbc.Driver";
                    String url = "jdbc:mysql://test/kollus_base?autoReconnect=true&useSSL=false";
                    String user = "root";
                    String password = "test";

                    Class.forName(driverName);
                    connection = DriverManager.getConnection(url, user, password);
                    statement = connection.createStatement();

                    resultSet = statement.executeQuery(sql);
                    resultSet.last();
                    int rowcount = resultSet.getRow();
                    resultSet.beforeFirst();

                    progressBar1.setMaximum(rowcount);
                    JwtEncoder encoder = new JwtEncoder();
                    int success_cnt = 0;
                    int fail_cnt = 0;
                    int exptMinutes = 2;
                    run.setText("Stop");

                    while( resultSet.next() && stopValue){
                        String m_key = resultSet.getString(1);

                        MediaItem item1 = new MediaItem();
                        item1.setMckey(m_key);
                        MediaItem[] items = new MediaItem[]{item1};
                        String url2 = encoder.getPlayUrl(host, userKey, cuid, secretKey, exptMinutes, items);

                        System.out.println("1-Time URL : " + url2+"&purge_cache");

                        URL url_purge = new URL(url2+"&purge_cache");
                        HttpURLConnection con = (HttpURLConnection) url_purge.openConnection();
                        con.setRequestMethod("GET");

                        if(con.getResponseMessage().equals("OK")){
                            System.out.println(con.getResponseMessage());
                            success_cnt++;
                        }else{
                            fail_cnt++;
                            System.out.println("=========실패=================="+con.getResponseMessage());
                        }
                        con.disconnect();
                        progressBar1.setValue(success_cnt);
                    }

                    stopValue = true;
                    System.out.println("성공 카운드 : "+success_cnt);
                    System.out.println("실패 카운드 : "+fail_cnt);

                }catch (ClassNotFoundException e){
                    System.out.println("[로드 오류]\n" + e.getMessage());
                }catch (SQLException e){
                    System.out.println("[연결 오류]\n" +  e.getMessage());
                }catch (Exception e1){
                    run.setText("RUN");
                    JOptionPane.showMessageDialog(null, "Purge를 실패했습니다. \n Error : "+e1.getMessage());
                    stateValue = false;
                    System.out.println(e1.getMessage());
                }finally {
                    try{
                        if( connection != null ){
                            connection.close();
                        }
                        if( statement != null ){
                            statement.close();
                        }
                        if( resultSet != null ){
                            resultSet.close();
                        }
                    }catch (SQLException e){
                        System.out.println("[닫기 오류]\n" +  e.getMessage());
                    }
                }

                String res = "Finished Execution";
                return res;
            }

            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread
                // will do with the intermediate results received
                // while the thread is executing
                //  int val = chunks.get(chunks.size()-1);

                // statusLabel.setText(String.valueOf(val));
            }

            @Override
            protected void done()
            {
                run.setText("RUN");
                System.out.println("Inside done function");
                if(stateValue){
                    JOptionPane.showMessageDialog(null, "purge를 완료 했습니다!");
                }else{
                    stateValue = true;
                }

            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }

}
