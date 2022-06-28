package com.omoniyi24.lnjshell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class LnjShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(LnjShellApplication.class, args);
	}

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person{

	private Long id;
	private String name;

}

@Service
class ConsoleService{

	private final static String ANSI_YELLOW = "\u001B[33m";
	private final static String ANSI_RESET = "\u001B[0m";

	private final PrintStream out = System.out;

	public void write(String msg, String... args){
		this.out.print("> ");
		this.out.print(ANSI_YELLOW);
		this.out.printf(msg, (Object[]) args );
		this.out.print(ANSI_RESET);
		this.out.println();
	}
}


@Service
class PersonService implements InitializingBean {

	private final Map<Long, Person> people = new ConcurrentHashMap<>();
	private final AtomicBoolean connected = new AtomicBoolean();

	boolean isConnected(){
		return this.connected.get();
	}

	void connect(String usr, String pw){
		this.connected.set(true);
	}

	void disconnect(){
		this.connected.set(false);
	}

	Person findById(Long id){
		return this.people.get(id);
	}

	Collection<Person> findByName(String name){
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
	}
}


@Component
class ConnectedPromptProvider implements PromptProvider{

	private final PersonService personService;

	ConnectedPromptProvider(PersonService personService) {
		this.personService = personService;
	}

	@Override()
	public AttributedString getPrompt(){
		String msg = String.format("lnj-shell %s)> ", this.personService.isConnected() ? "connected" : "disconnect");
		return new AttributedString(msg);
	}
}


@ShellComponent
class ConnectionCommands {

	private final PersonService personService;
	private final ConsoleService  consoleService;
	private final LDJService  ldjService;


	ConnectionCommands(PersonService personService, ConsoleService consoleService, LDJService ldjService){
		this.personService = personService;
		this.consoleService = consoleService;
		this.ldjService = ldjService;
	}

	@ShellMethod("connect to the person service")
	public void connect(String username, String password){
		this.personService.connect(username, password);
		this.consoleService.write("connected %s", username);
	}

	Availability connectAvailability(){
		return !this.personService.isConnected() ? Availability.available() : Availability.unavailable("you're already connected");
	}

	@ShellMethod("disconnect to the person service")
	public void disconnect(){
		this.personService.disconnect();
		this.consoleService.write("disconnected %s");
	}

	Availability disconnectAvailability(){
		return this.personService.isConnected() ? Availability.available() : Availability.unavailable("you're not connected");
	}


}