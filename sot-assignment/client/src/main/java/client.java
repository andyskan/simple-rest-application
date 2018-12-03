//
//import goodHouse.rest.model.room;
//import goodHouse.rest.model.student;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
//import sot.rest.service.model.student;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class client {

    static URI baseURI = UriBuilder.fromUri("http://localhost:9090/goodHouse/rest/rooms").build();//8989 is port for burp
    static Client client = ClientBuilder.newClient(new ClientConfig()).register(JacksonFeature.class);
    static WebTarget serviceTarget = client.target(baseURI);

    static URI studentUri = UriBuilder.fromUri("http://localhost:9090/goodHouse/rest/student").build();
    static Client studentClient = ClientBuilder.newClient(new ClientConfig()).register(JacksonFeature.class);;
    static WebTarget studentTarget = client.target(studentUri);


    public static void main(String[] args) {
        sayHello();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1.Student Page");
            System.out.println("2.Admin Page");
            System.out.println("3.Exit");
            System.out.print("Pick menu : ");
            int menu = sc.nextInt();
            if (menu == 1) studentMenu();
            else if (menu == 2) adminMenu();
            else if (menu == 3) break;
            else System.out.println("Selection invalid, please select again");
        }
    }

    public static void studentMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1.Login");
            System.out.println("2.Register");
            System.out.println("3.Exit");
            System.out.print("Pick menu : ");
            int menu = Integer.parseInt(sc.nextLine());

            if (menu == 1) {
                student murid = studentLogin();
                if (murid != null) {
                    while (true) {
                        System.out.println("1.View all Rooms");
                        System.out.println("2.Book a room");
                        System.out.println("3.Update data");
                        System.out.println("4.Exit");

                        System.out.print("Pick menu : ");
                        int pick = Integer.parseInt(sc.nextLine());

                        if (pick == 1) getAllRoom();
                        else if (pick == 2) assignStudent(murid);
                        else if (pick == 3) updateStudentSelf(murid.getId());
                        else if (pick == 4) break;
                        else System.out.println("Menu invalid, please pick again");
                    }
                } else System.out.println("username or email does not match");
            } else if (menu == 2) createStudent();
            else if (menu == 3) break;
        }
    }

    public static student studentLogin() {
        String email, password;
        Scanner sc = new Scanner(System.in);
        System.out.print("Email :");
        email = sc.nextLine();
        System.out.print("Password :");
        password = sc.nextLine();
        Invocation.Builder requestBuilder = studentTarget.path("json").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<student>> genericType = new GenericType<>() {
        };
        ArrayList<student> entity = response.readEntity(genericType);
        student selected = null;
        for (student murid : entity) {
            if ((murid.getEmail().equals(email))
                    && murid.getPassword().equals(password)) {
                selected = murid;
            }
        }
        return selected;
    }

    public static void adminMenu() {
        Scanner sc = new Scanner(System.in);
        if (adminLogin()) {
            while (true) {
                System.out.println("1.Student Settings");
                System.out.println("2.Room Settings");
                System.out.println("3.Exit");
                System.out.print("Pick menu : ");
                int menu = sc.nextInt();

                if (menu == 1) studentSettings();
                else if (menu == 2) roomMenu();
                else if (menu == 3) break;
                else System.out.println("menu does not exist");
            }
        }
    }

    public static boolean adminLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print("username :");
        String uname = sc.nextLine();
        System.out.print("password :");
        String pass = sc.nextLine();

        if (pass.equals("admin") && uname.equals("admin")) return true;
        else return false;

    }

    public static void roomMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1.View All Rooms");
            System.out.println("2.Add new Room");
            System.out.println("3.Update Room");
            System.out.println("4.Delete Room");
            System.out.println("5.Assign Room");
            System.out.println("6.Break");
            System.out.print("Pick your choice : ");
            Integer i = sc.nextInt();
            if (i == 1) getAllRoom();
            else if (i == 2) createRoom();
            else if (i == 3) updateRoom();
            else if (i == 4) deleteRoom();
            else if (i == 5) assignRoom();
            else if (i == 6) break;
            else System.out.println("menu does not exist");

//        System.out.println("4.View All Rooms");
        }
    }

    public static void studentSettings() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("1.View All Student");
            System.out.println("2.Add new Student");
            System.out.println("3.Update Student");
            System.out.println("4.Delete Student");
            System.out.println("5.Break");
            System.out.print("Pick your choice : ");
            Integer i = Integer.parseInt(sc.nextLine());
            if (i == 1) getAllStudent();
            else if (i == 2) createStudent();
            else if (i == 3) updateStudent();
            else if (i == 4) deleteStudent();
            else if (i == 5) break;
            else System.out.println("menu does not exist");
        }
//        System.out.println("4.View All Rooms");

    }

    public static void sayHello() {
        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String entity = response.readEntity(String.class);
            System.out.println("say hello returned: " + entity);

        } else {
            System.err.println("Calling sayHello: ( caused an error");
            System.err.println(response);
            System.err.println("Entity: " + response.readEntity(String.class));
        }
    }

    public static void updateRoom() {
        clearScreen();
        getAllRoom();
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose room to update : ");
        int roomId = Integer.parseInt(sc.nextLine());

        System.out.println("New Location: ");
        String location = sc.nextLine();

        System.out.print("New width : ");
        int width = sc.nextInt();
        System.out.print("New Price : ");
        int price = sc.nextInt();

        Form form = new Form();
        form.param("id", Integer.toString(roomId));
        form.param("width", Integer.toString(width));
        form.param("loc", location);
        form.param("price", Integer.toString(price));

        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Update Room success! ");
        } else System.out.println("Update Room Error : " + response);
    }

    public static void createRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Room Location: ");
        String location = sc.nextLine();
        System.out.print("Width : ");
        int width = sc.nextInt();
        System.out.print("Price : ");
        int price = sc.nextInt();


        room kamer = new room(roomSize() + 1, width, location, price);
        Entity<room> entity = Entity.entity(kamer, MediaType.APPLICATION_JSON);
        //create http request
        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.post(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Create room succedeed!");
        } else System.out.println("Error : " + response);
    }

    public static void deleteRoom() {
        getAllRoom();
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose Room to delete : ");
        int id = Integer.parseInt(sc.nextLine());
        Invocation.Builder requestBuilder = serviceTarget.path(Integer.toString(id)).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Delete Room success");
        } else System.out.println("Delete Room Error : " + response);
    }

    public static void getAllRoom() {
        //Invocation.Builder requestBuilder = serviceTarget.path("json").path("first").request().accept(MediaType.APPLICATION_JSON);
        Invocation.Builder requestBuilder = serviceTarget.path("json").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<room>> genericType = new GenericType<>() {
            };
            ArrayList<room> entity = response.readEntity(genericType);
            final Object[][] table = new String[roomSize() + 1][];
            int count = 1;
            table[0] = new String[]{"id", "Location", "Width", "Price", "Occupier"};
            for (room kamer : entity) {
                table[count++] = new String[]{
                        Integer.toString(kamer.getId()),
                        kamer.getLocation(),
                        Integer.toString(kamer.getWidth()),
                        Integer.toString(kamer.getPrice()),
                        kamer.getOccupier()
                };
            }
            for (final Object[] row : table) {
                System.out.format("%15s%15s%15s%15s%15s\n", row);
            }
        } else {
            System.err.println("getAllStudent : " + response);
        }
    }

    public static int roomSize() {
        Invocation.Builder requestBuilder = serviceTarget.path("count").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        Integer zero = 0;
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Integer entity = response.readEntity(Integer.class);
            return entity;
        } else {
//            System.err.println("count " + response);
            return zero;
        }
    }

    public static void assignRoom() {
        Scanner sc = new Scanner(System.in);
        getAllRoom();
        System.out.print("Pick room to assign");
        Integer selectedRoom = Integer.parseInt(sc.nextLine());
        getAllStudent();
        System.out.print("Pick student to assign");
        int selectedStudent = Integer.parseInt(sc.nextLine());
        String studentName = getStudent(selectedStudent).getName();
        Form form = new Form();
        form.param("id", selectedRoom.toString());
        form.param("name", studentName);


        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
        Invocation.Builder requestBuilder = serviceTarget.path("occupier").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Update Room success! ");
        } else System.out.println("Update Room Error : " + response);

    }

    public static void assignStudent(student murid) {
        Scanner sc = new Scanner(System.in);
        getAllRoom();
        System.out.print("Pick room to assign");
        Integer selectedRoom = Integer.parseInt(sc.nextLine());
//        getAllStudent();
//        System.out.print("Pick student to assign");
//        int selectedStudent = Integer.parseInt(sc.nextLine());
        String studentName = murid.getName();
        Form form = new Form();
        form.param("id", selectedRoom.toString());
        form.param("name", studentName);


        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
        Invocation.Builder requestBuilder = serviceTarget.path("occupier").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Your room has been booked! ");
        } else System.out.println("Book room Error : " + response);
    }


    public static void getAllStudent() {
        Invocation.Builder requestBuilder = studentTarget.path("json").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            final Object[][] table = new String[studentSize() + 1][];
            int count = 1;
            table[0] = new String[]{"id", "Name", "Email", "Password"};
            GenericType<ArrayList<student>> genericType = new GenericType<>() {
            };
            ArrayList<student> entity = response.readEntity(genericType);
            for (student murid : entity) {
                table[count++] = new String[]{
                        Integer.toString(murid.getId()),
                        murid.getName(),
                        murid.getEmail(),
                        murid.getPassword()
                };
            }
            for (final Object[] row : table) {
                System.out.format("%15s%20s%20s%20s\n", row);
            }
        } else {
            System.err.println("getAllStudent : " + response);
        }
    }

    public static student getStudent(int id) {
        Invocation.Builder requestBuilder = studentTarget.queryParam("id", id).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            student entity = response.readEntity(student.class);
            return entity;
        } else return null;
    }

    public static void deleteStudent() {
        getAllStudent();
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose Student to delete : ");
        String studentId = sc.nextLine();
        Invocation.Builder requestBuilder = studentTarget.path(studentId).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Delete success");
        } else System.out.println("Delete student : " + response);
    }


    public static int studentSize() {
        Invocation.Builder requestBuilder = studentTarget.path("count").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        Integer zero = 0;
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Integer entity = response.readEntity(Integer.class);
            return entity;
        } else {
            System.err.println("count Error :  " + response);
            return zero;
        }
    }

    public static void createStudent() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Student Name: ");
        String name = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("password : ");
        String password = sc.nextLine();

        student murid = new student(studentSize() + 1, name, email, password);
        Entity<student> entity = Entity.entity(murid, MediaType.APPLICATION_JSON);
        //create http request
        Invocation.Builder requestBuilder = studentTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.post(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Create student succedeed!");
        } else System.out.println("Error : " + response);
    }

    public static void updateStudent() {

        getAllStudent();
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose student to update : ");
        int studentId = Integer.parseInt(sc.nextLine());
        System.out.print("Student Name: ");
        String name = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("password : ");
        String password = sc.nextLine();

        student murid = new student(studentId, name, email, password);

        Entity<student> entity = Entity.entity(murid, MediaType.APPLICATION_JSON);

        Invocation.Builder requestBuilder = studentTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("UpdateStudent ok");
        } else {
            System.out.println("UpdateStudent not ok : " + response);
        }
    }

    public static void updateStudentSelf(int id) {

//        getAllStudent();
        Scanner sc = new Scanner(System.in);
//        System.out.print("Choose student to update : ");
//        int studentId = murid.getId();
        System.out.print("Student Name: ");
        String name = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("password : ");
        String password = sc.nextLine();

        student murid = new student(id, name, email, password);

        Entity<student> entity = Entity.entity(murid, MediaType.APPLICATION_JSON);

        Invocation.Builder requestBuilder = studentTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("UpdateStudent ok");
        } else {
            System.out.println("UpdateStudent not ok : " + response);
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}



