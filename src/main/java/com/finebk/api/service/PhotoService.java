package com.finebk.api.service;

import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.PagedResponse;
import com.finebk.api.payload.PhotoRequest;
import com.finebk.api.payload.PhotoResponse;
import com.finebk.api.security.UserPrincipal;

public interface PhotoService {

	PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

	PhotoResponse getPhoto(Long id);

	PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

	PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

	ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

	PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);

}