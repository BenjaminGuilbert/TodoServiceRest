package iad.rest.todo;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

@Path("/todos")
public class TodosResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Todo> getTodos(){
		List<Todo> todos = new LinkedList<Todo>();
		//Todo td = new Todo("1", "Faire les courses",new Date().toString(), Collections.singletonList("Important"));
		//todos.add(td);
		todos.addAll(Todo.valuesStore());
		System.out.println("Nb Todos : "+Todo.valuesStore().size());
		return todos;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createTodo(JAXBElement<Todo> xmlTodo){
		Todo todo = xmlTodo.getValue();
		Response resp;
		String id = todo.getId();
		if(Todo.containStore(id)){ //if todo already exist, send a no content message 
			resp = Response.noContent().build();
		}
		else {
			Todo.putStore(id, todo);
			String uriBase = uriInfo.getAbsolutePath().toString();
			URI uriTodo = URI.create(uriBase + "/" +id);
			resp = Response.created(uriTodo).build();
		}
			
		return resp;
		
	}
	
	@Path("/{todoid}")
	@DELETE
	public Response deleteTodo(@PathParam("todoid") String id) {
		Response resp;
		if(Todo.containStore(id)) {
			Todo.removeStore(id);
			resp = Response.noContent().build();
		}
		else {
			resp = Response.status(Status.NOT_FOUND).build();
		}
		return resp;
	}
	
	@Path("/{todoid}")
	@PUT
	public Response addTodo(@PathParam("todoid") String id,JAXBElement<Todo> xmlTodo) {
		Response resp;
		Todo todo = xmlTodo.getValue();
		String todoId = todo.getId();
		if(todoId.equals(id) && Todo.containStore(id)) {
			Todo.putStore(todoId, todo);
			resp = Response.noContent().build();
		}
		else {
			resp = Response.status(Status.NOT_FOUND).build();
		}
		return resp;
	}
	

}
