package com.fsd.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import com.fsd.pm.service.dto.ProjectDto;
import com.fsd.pm.service.dto.UserDto;

public class ProjectControllerIntegrationTests {

	private static final String BASE_USER_URI = "http://localhost:8080/projectManagement/user";

	private static final String BASE_PROJECT_URI = "http://localhost:8080/projectManagement/project";

	private Client client = ClientBuilder.newClient();

	public static final int HTTP_CREATED = 201;

	public static final int HTTP_DELETED = 204;

	public ProjectControllerIntegrationTests() {

	}

	public ProjectControllerIntegrationTests(String strParam) {
		this();
	}

	@Test
	public void testProjectOperations() {

//		// Create User
		UserDto userDto = new UserDto();
		userDto.setFirstName("Rohit");
		userDto.setLastName("Yadav");

		Response response = client.target(BASE_USER_URI + "/create").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(userDto, MediaType.APPLICATION_JSON));
		assertEquals(response.getStatus(), HTTP_CREATED);

		// Find User
		String userPath = response.getLocation().toString();
		UserDto newUser = client.target(userPath).request(MediaType.APPLICATION_JSON).get(UserDto.class);
		assertThat(newUser, IsNull.notNullValue());
		assertThat(newUser.getFirstName(), IsEqual.equalTo("Rohit"));

		// Create Project
		ProjectDto projectDto = new ProjectDto();
		projectDto.setProject("MyProject");
		projectDto.setManager(userDto);

		response = client.target(BASE_PROJECT_URI + "/create").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(projectDto, MediaType.APPLICATION_JSON));
		assertEquals(response.getStatus(), HTTP_CREATED);

		// Find Project
		String projectPath = response.getLocation().toString();
		ProjectDto newProject = client.target(projectPath).request(MediaType.APPLICATION_JSON).get(ProjectDto.class);
		assertThat(newProject, IsNull.notNullValue());
		assertThat(newProject.getProject(), IsEqual.equalTo("MyProject"));

		// Delete Project
		Response deleteResponse = client.target(BASE_PROJECT_URI).path(String.valueOf(newProject.getProjectId()))
				.request(MediaType.APPLICATION_JSON).delete();
		assertEquals(deleteResponse.getStatus(), HTTP_DELETED);

		// Delete User
		deleteResponse = client.target(BASE_USER_URI).path(String.valueOf(newUser.getUserId()))
				.request(MediaType.APPLICATION_JSON).delete();
		assertEquals(deleteResponse.getStatus(), HTTP_DELETED);

		System.out.println("Successfully executed test!");

	}

}
