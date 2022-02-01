package com.sopromadze.blogapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserIdentityAvailability {
	private Boolean available;

}
