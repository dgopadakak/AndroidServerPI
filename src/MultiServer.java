import Departments.Courier;
import Departments.DepartmentOperator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer
{
    private ServerSocket serverSocket;
    private static DepartmentOperator deo = new DepartmentOperator();
    private static String deoJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            deoJSON = readFile(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        deo = gson.fromJson(deoJSON, DepartmentOperator.class);

//        deo.addCourier("Восточный", new Courier("Иванов Иван Иванович", "Автомобиль", 7,
//                "20.07.2001", "06.11.2019", 3, 1,
//                "Вежливый курьер с большим опытом."));
//        deo.addCourier("Восточный", new Courier("Сергеенко Сергей Сергеев", "Велосипед", 4,
//                "11.11.2003", "12.06.2022", 6, 0,
//                "Начинающий курьер."));
//
//        deo.addCourier("Западный", new Courier("Ильин Илья Ильич", "Самокат", 11,
//                "04.02.2000", "10.10.2020", 3, 0,
//                "Быстрый, но не очень вежлевый курьер."));
//        deo.addCourier("Западный", new Courier("Антонов Антон Антонович", "Автомобиль", 23,
//                "26.11.1996", "11.10.2015", 15, 1,
//                "Очень быстрый и вежливый курьер, лучший работник месяца."));
//
//        deoJSON = gson.toJson(deo);
//        writeFile(filePath, deoJSON);
//        System.out.println("Done!");

        serverSocket = new ServerSocket(port);
        while (true)
        {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true)
            {
                try
                {
                    if ((inputLine = in.readLine()) == null) break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (".".equals(inputLine))
                {
                    out.println("bye");
                    break;
                }
                if ("REFRESH".equals(inputLine))
                {
                    out.println(deoJSON);
                }
                if (inputLine != null)
                {
                    if ('d' == inputLine.charAt(0))     // d0,1
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        deo.delCourier(groupID, examID);
                        deoJSON = gson.toJson(deo);
                        writeFile(filePath, deoJSON);
                        out.println(deoJSON);
                    }
                    if ('e' == inputLine.charAt(0))     // e0,3##json
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        Courier tempCourier = gson.fromJson(parts[1], Courier.class);
                        deo.editCourier(groupID, examID, tempCourier);
                        deoJSON = gson.toJson(deo);
                        writeFile(filePath, deoJSON);
                        out.println(deoJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        DepartmentOperator tempGo = gson.fromJson(inputLine.substring(1), DepartmentOperator.class);
                        deo.setDepartments(tempGo.getDepartments());
                        deoJSON = gson.toJson(deo);
                        writeFile(filePath, deoJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Courier tempCourier = gson.fromJson(parts[1], Courier.class);
                        deo.addCourier(parts[0], tempCourier);
                        deoJSON = gson.toJson(deo);
                        writeFile(filePath, deoJSON);
                        out.println(deoJSON);
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            out.close();
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text)
    {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
