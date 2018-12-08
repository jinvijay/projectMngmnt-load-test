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

import com.fsd.pm.service.dto.UserDto;

public class UserControllerIntegrationTests {

	private static final String BASE_URI = "http://localhost:8080/projectManagement/user";

	private Client client = ClientBuilder.newClient();

	public static final int HTTP_CREATED = 201;

	public static final int HTTP_DELETED = 204;

	public UserControllerIntegrationTests() {

	}

	public UserControllerIntegrationTests(String strParam) {
		this();
	}

	@Test
	public void testUserOperations() {

		// Create User
		UserDto userDto = new UserDto();
		userDto.setFirstName("Rohit");
		userDto.setLastName("Yadav");

		Response response = client.target(BASE_URI + "/create").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(userDto, MediaType.APPLICATION_JSON));
		assertEquals(response.getStatus(), HTTP_CREATED);

		String userPath = response.getLocation().toString();

		// Find User
		UserDto newUser = client.target(userPath).request(MediaType.APPLICATION_JSON).get(UserDto.class);
		assertThat(newUser, IsNull.notNullValue());
		assertThat(newUser.getFirstName(), IsEqual.equalTo("Rohit"));

		// Delete User
		Response deleteResponse = client.target(BASE_URI).path(String.valueOf(newUser.getUserId()))
				.request(MediaType.APPLICATION_JSON).delete();
		assertEquals(deleteResponse.getStatus(), HTTP_DELETED);

		System.out.println("Successfully executed test!");

	}

}
