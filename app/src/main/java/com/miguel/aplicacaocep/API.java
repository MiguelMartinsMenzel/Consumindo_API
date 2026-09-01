package com.miguel.aplicacaocep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API {
    @GET("{cep}/json")
    Call<CEP> getCep(@Path("cep") String Cep);
}

