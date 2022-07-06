package com.omoniyi24.lnjshell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.Availability;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class LnjShellApplication {

    public static void main(String[] args) {
        System.out.println(">>>>>> hello <<<<<<");
        SpringApplication.run(LnjShellApplication.class, args);
        System.out.println(">>>>>> goodbye <<<<<<");
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {

    private Long id;
    private String name;

}

@Service
class ConsoleService {

    private final static String ANSI_YELLOW = "\u001B[33m";
    private final static String ANSI_RESET = "\u001B[0m";

    private final PrintStream out = System.out;

    public void write(String msg, String... args) {
        this.out.print("> ");
        this.out.print(ANSI_YELLOW);
        this.out.printf(msg, (Object[]) args);
        this.out.print(ANSI_RESET);
        this.out.println();
    }
}


@Service
class LNJShellService implements InitializingBean {

    private final Map<Long, Person> people = new ConcurrentHashMap<>();
    private final AtomicBoolean connected = new AtomicBoolean();
    private LNJService LNJService;

    boolean isConnected() {
        return this.connected.get();
    }

    void connect(String usr, String pw) {
        this.connected.set(true);
    }

    void disconnect() {
        this.connected.set(false);
    }

    Person findById(Long id) {
        return this.people.get(id);
    }

    Collection<Person> findByName(String name) {
        return this.people.values()
                .stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AtomicLong ids = new AtomicLong();
        Map<Long, Person> personMap = Stream.of("Ilesanmi Omoniyi", "Akindele Oyindamola", "Jaja Opobo", "Seun Jay", "Tobi Ademola")
                .map(name -> new Person(ids.incrementAndGet(), name))
                .collect(Collectors.toMap(p -> p.getId(), p -> p));

        this.people.putAll(personMap);

//        ldjService = new LDJService();
//        ldjService.start();
        setLdjService(LNJService);
    }

    public LNJService getLdjService() {
        return LNJService;
    }

    public void setLdjService(LNJService LNJService) {
        this.LNJService = LNJService;
    }
}


@Component
class ConnectedPromptProvider implements PromptProvider {

    private final LNJShellService LNJShellService;

    ConnectedPromptProvider(LNJShellService LNJShellService) {
        this.LNJShellService = LNJShellService;
    }

    @Override()
    public AttributedString getPrompt() {
        String msg = String.format("lnj-shell (%s) > ", this.LNJShellService.isConnected() ? "connected" : "disconnect");
        return new AttributedString(msg);
    }
}


@ShellComponent
class ConnectionCommands {

    private final LNJShellService LNJShellService;
    private final ConsoleService consoleService;
    private final LnjServiceUtil lnjServiceUtil;
    public LNJService LNJService = new LNJService();


    ConnectionCommands(LNJShellService LNJShellService, ConsoleService consoleService, LNJService LNJService, LnjServiceUtil lnjServiceUtil) {
        this.LNJShellService = LNJShellService;
        this.consoleService = consoleService;
        this.lnjServiceUtil = lnjServiceUtil;
    }

    @ShellMethod("connect to lnj node")
    public void connect(String connectionString) throws Exception {


        String[] connectionStringSplit = connectionString.split("@");
        String peerPublicKey = connectionStringSplit[0];
        String peerAdderess = connectionStringSplit[1];
        String[] peerAdderessSplit = peerAdderess.split(":");
        String peerHost = peerAdderessSplit[0];
        int peerPort = Integer.parseInt(peerAdderessSplit[1]);
        LNJService.connect(peerPublicKey, peerHost, peerPort);
        this.LNJShellService.isConnected();
        this.consoleService.write("connected to %s", peerPublicKey);
    }

    @ShellMethod("connect to lnj node")
    public void listpeers() throws Exception {
        try {
            byte[][] peer_node_ids = LNJService.listPeers();
            final var byteArray = new ArrayList<>(List.of(peer_node_ids));
            StringBuffer hexStringBuffer = new StringBuffer();
            for (int i = 0; i < byteArray.size(); i++) {
                hexStringBuffer.append(Hex.encodeHex(byteArray.get(i)));
            }
            this.consoleService.write(String.valueOf(hexStringBuffer));
//            System.out.print(hexStringBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ShellMethod("connect to lnj node")
    public void createinvoice(Long amountInMSat, String description) throws Exception {
        try {
            String invoice = LNJService.generateInvoice(amountInMSat, description);
            this.consoleService.write(invoice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Availability connectAvailability() {
        return !this.LNJShellService.isConnected() ? Availability.available() : Availability.unavailable("you're already connected");
    }

    @ShellMethod("disconnect to the person service")
    public void disconnect() {
        this.LNJShellService.disconnect();
        this.consoleService.write("disconnected %s");
    }

    @ShellMethod("start LDK")
    public void start() {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(new Runnable() {
            public void run() {
                try {
                    System.out.println("Starting LNJ Node Service 🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬🇳🇬");
                    System.out.println(String.format("LND NODE :[ %s, %s]",
                            "02fb85064a8f6c75655cdd8189b7c072dea0bcc90874abc1b130d9d951eb9c17ca", "127.0.0.1:9731"));
                    LNJService.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(">>>>>> exception <<<<<<");
                }
                executorService.shutdown();
            }
        });
    }

    Availability disconnectAvailability() {
        return this.LNJShellService.isConnected() ? Availability.available() : Availability.unavailable("you're not connected");
    }
}
