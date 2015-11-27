package com.instituto.cuanto.sisgene.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.instituto.cuanto.sisgene.EnvioService;
import com.instituto.cuanto.sisgene.dao.EnviarEncuestaDAO;
import com.instituto.cuanto.sisgene.entidad.Allegado;
import com.instituto.cuanto.sisgene.entidad.CabeceraEncuestaRpta;
import com.instituto.cuanto.sisgene.entidad.DetalleEncuestaRpta;
import com.instituto.cuanto.sisgene.entidad.DireccionViviendaEncuestada;
import com.instituto.cuanto.sisgene.entidad.PersonaEncuestada;
import com.instituto.cuanto.sisgene.forms.GuardarEncuestaRequest;
import com.instituto.cuanto.sisgene.forms.GuardarEncuestaResponse;
import com.instituto.cuanto.sisgene.forms.ValidarAdministradorResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.net.URLEncoder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.FormUrlEncoded;

/**
 * Created by Jesus on 19/11/2015.
 */
public class EnvioServiceUtil {

    Context context;
    String idCabEnc = "",estadoWS = "",ip="",puerto="";
    Gson gson = new Gson();

    public EnvioServiceUtil(){}

    public boolean enviarEncuestaEjecutada(Context context, String idCabeceraEncuesta){

        System.out.println("IDCAB : "+idCabeceraEncuesta);

        this.context = context;
        this.idCabEnc = idCabeceraEncuesta;

        try {
            LeerProperties leerProperties = new LeerProperties();
            String ipWS = leerProperties.leerIPWS();
            String puertoWS = leerProperties.leerPUERTOWS();
            ip = ipWS;
            puerto = puertoWS;

            if(ipWS != null && puertoWS != null){
                new RestCosumeAsyncTask().execute();
            }else{
                Toast.makeText(context, "No se encuentra el archivo de configuracion", Toast.LENGTH_LONG).show();
            }

            if (estadoWS.equals("00")) {
                return true;
            } else {
                return false;
            }
        }catch(Exception e){
            System.out.println("ERROR EN ENVIO DE INF : "+e.getMessage());
            return false;
        }
    }

    private class RestCosumeAsyncTask extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... params) {

            EnviarEncuestaDAO enviarEncuestaDAO = new EnviarEncuestaDAO();
            GuardarEncuestaRequest guardarRequest = new GuardarEncuestaRequest();

            PersonaEncuestada persona = enviarEncuestaDAO.obtenerPersonaEncuestada(context,idCabEnc);
            DireccionViviendaEncuestada direccion = enviarEncuestaDAO.obtenerDireccionEncuestada(context, idCabEnc);
            CabeceraEncuestaRpta cabeceraEncuestaRpta = enviarEncuestaDAO.obtenerCabEncRtpaEncuestada(context, idCabEnc);
            List<DetalleEncuestaRpta> detalleEncuestaRptas = enviarEncuestaDAO.obtenerDenEncEncuestada(context, idCabEnc);
            List<Allegado> listAllegado = enviarEncuestaDAO.obtenerAllegadoEncuestada(context, idCabEnc);

            guardarRequest.setPersona_encuestada(persona);
            guardarRequest.setDireccion_viviendaencuestada(direccion);
            guardarRequest.setCab_enc_rpta(cabeceraEncuestaRpta);
            guardarRequest.setLista_det_enc_rpta(detalleEncuestaRptas);
            guardarRequest.setLista_allegados(listAllegado);

            String jsonEnviar = gson.toJson(guardarRequest);
            System.out.println("JASON ENVIAR : "+jsonEnviar);

            try {
                String a = URLEncoder.encode(jsonEnviar, "UTF-8");
                jsonEnviar = a;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://"+ip+":"+puerto+"/resources/WebServiceSISGENE")
                    .build();


            EnvioService service = restAdapter.create(EnvioService.class);

            service.repository2Sync(jsonEnviar, new Callback<GuardarEncuestaResponse>() {
                @Override
                public void success(GuardarEncuestaResponse guardarEncuestaResponse, Response response) {
                    System.out.println("1.- "+guardarEncuestaResponse.getCodigo_respuesta());
                    System.out.println("2.- "+guardarEncuestaResponse.getMensaje());
                    if (guardarEncuestaResponse.getCodigo_respuesta().equals("01")){
                        estadoWS = "01";
                        progressDialog.hide();
                        Toast.makeText(context, guardarEncuestaResponse.getMensaje(), Toast.LENGTH_LONG).show();
                    }else {
                        estadoWS = "00";
                        progressDialog.hide();
                        Toast.makeText(context, "Envio de Encuesta con éxito", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    estadoWS = "01";
                    Toast.makeText(context, "Ocurrio un error en el envio de Información, verifique su conexión a Internet", Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });

            System.out.println("TERMINANDO DE EJECUTAR");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("PREEXECUTE");
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Enviando información");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("POSTEXCECUTE");
            //progressDialog.hide();
        }
    }
}
