package com.xebia.fs101.writerpad.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "unplashImageClient", url =
        "https://api.unsplash"
                + ".com/photos/random/?client_id"
                + "=df1deb685431c6c5144871867a9d4dec0a3de375cf74a9161b477c726f6db561\n")
public interface ImageFinderService {
//    @RequestMapping(method = RequestMethod.GET, path = "/")
//    Resource<Images> getImage();
}
