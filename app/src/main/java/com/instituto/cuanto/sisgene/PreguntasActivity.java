package com.instituto.cuanto.sisgene;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.instituto.cuanto.sisgene.adapter.TipoPreguntaAbiertaAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMatrizMultipleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMatrizSimpleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMixtaAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMultipleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaUnicaAdapter;
import com.instituto.cuanto.sisgene.bean.EncuestaPregunta;
import com.instituto.cuanto.sisgene.dao.EncuestaDAO;
import com.instituto.cuanto.sisgene.entities.MixtaAlternativa;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaAbiertaItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMatrizItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMixtaItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMultipleItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaUnicaItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by Gustavo on 09/10/2015.
 */
public class PreguntasActivity extends AppCompatActivity {

    static ArrayList<String> nombresEncuestados;
    static ArrayList<Integer> codigosIdentEncuestados;
    Button btnSiguiente;
    ListView lvRespuestas_tipoGeneral;
    Context context = PreguntasActivity.this;

    ScrollView scrollView;
    TextView tvEnunciadoPregunta;
    TextView tvSeccion;
    TextView tvSubSeccion;
    TextView tvOpcionesPregunta;


    //variables que seran cambiadas de pregunta en pregunta
    int numPregunta = -1;
    int mumMaxChequeados;
    String tipoPreguntaActual;
    boolean encuestarTodos = true;
    boolean ordenImportancia;
    String nombreSecccion;
    String nommbreSubSeccion;
    String descSeccion;
    String descSubSeccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypreguntas);

        nombresEncuestados = new ArrayList<>();
        codigosIdentEncuestados = new ArrayList<>();
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        //lyFragmentoListaPreguntas = (LinearLayout) findViewById(R.id.lyFragmentoListaPreguntas);
        lvRespuestas_tipoGeneral = (ListView) findViewById(R.id.lvRespuestas_tipoGeneral);
        tvEnunciadoPregunta = (TextView) findViewById(R.id.tvEnunciadoPregunta);
        tvOpcionesPregunta = (TextView) findViewById(R.id.tvOpcionesPregunta);
        tvSeccion = (TextView) findViewById(R.id.tvSeccion);
        tvSubSeccion = (TextView) findViewById(R.id.tvSubSeccion);
        TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter = new TipoPreguntaAbiertaAdapter();
        TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter = new TipoPreguntaUnicaAdapter();
        //miListaTipoPreguntaAbierta = new ArrayList<>();
        scrollView = new ScrollView(PreguntasActivity.this);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(NombresPersonasEncuestadasActivity.KEY_ARG_NOMBRES_ENCUESTADOS)) {
            System.out.println("se obtienen datos - nombres y codigos de indentificacion");
            nombresEncuestados = getIntent().getStringArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_NOMBRES_ENCUESTADOS);
            codigosIdentEncuestados = getIntent().getIntegerArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_ID_ENCUESTADOS);
        }

        btnSiguiente.setOnClickListener(btnSiguientesetOnClickListener);

        leerPrimeraPregunta();        //leer todos los datos de la primera pregunta
        tipoPreguntaActual = "MI";
        leerTipoPreguntaxPregunta();
    }

    private void leerPrimeraPregunta() {

        EncuestaDAO encuestaDAO = new EncuestaDAO();
        EncuestaPregunta encuestaPregunta;

        //ir a BD para sacar la primera preguna de dicha ecuesta
        encuestaPregunta = encuestaDAO.obtenerPreguntaEncuesta(PreguntasActivity.this);

        // Setear datos para la primera pregunta
        //encuestarTodos = Boolean.valueOf(encuestaPregunta.getPre_unica_persona());
        //nombreSecccion = encuestaPregunta.getSec_numero_seccion();
        //nombreSecccion = encuestaPregunta.getPre_importarordenrptamu();
    }

    View.OnClickListener btnSiguientesetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            leerDatos();
            System.out.println("respuestas leidas -   se borra la lista");
            leerTipoPreguntaxPregunta();
        }
    };

    private void leerTipoPreguntaxPregunta() {
        //eliminar cuando se haya hecho la consulta a la base de datos

        encuestarTodos = true;

        //Tipo de pregunta Unica
        if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaUnica))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaUnica();

            //Tipo de pregunta Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMultiple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMultiple();

            //Tipo de pregunta abierta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaAbierta))) {
            //TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaAbierta();

            //Tipo de pregunta Matriz Simple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatSimple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMatrizSimple();

            //Tipo de pregunta Matriz Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatMultiple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMatrizMultiple();

            //Tipo de pregunta Mixta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMixta))) {
            // tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMixta();
        }
    }


    private void leerDatos() {
        //Leer los dato segun el tipo de pregunta actual


        if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaUnica))) {
            leerRespuestasTipoUnica();

        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMultiple))) {
            leerRespuestasTipoMultiple();

        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaAbierta))) {
            leerRespuestasTipoAbierta();

        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatSimple))) {


        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatMultiple))) {


        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMixta))) {
            leerRespuestasTipoMixta();

        }
    }


    private void leerRespuestasTipoAbierta() {
        String respuestaAbierta = "";
        System.out.println("dimencion myListPreguntaAbierta:" + TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size());

        for (int i = 0; i < TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size(); i++) {
            TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.getItem(i);

            //agregar el codigo de identificacion
            Formatter codIdent = new Formatter();
            //codIdent.format("%02d", codigosIdentEncuestados.get(i));
            //respuestaAbierta = "[" + codIdent + "]";
            respuestaAbierta = respuestaAbierta + "[" + "]";
            //en caso el usuario no haya escrito nada
            if (nombresEncuestados.get(i).trim().equals(tipoPreguntaAbiertaItem.getDescription().trim())) {
                respuestaAbierta = respuestaAbierta + "null";
            } else
                respuestaAbierta = respuestaAbierta + tipoPreguntaAbiertaItem.getDescription();

            if (i != TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size() - 1)
                respuestaAbierta = "" + respuestaAbierta + "&";
        }

        System.out.println("respuesta: {" + respuestaAbierta + "}");

        //guardar en base de datos la respuesta


    }

    private void leerRespuestasTipoUnica() {
        String respuestaUnica = "";

        for (int i = 0; i < TipoPreguntaUnicaAdapter.myListPreguntaUnica.size(); i++) {
            TipoPreguntaUnicaItem tipoPreguntaUnicaItem = TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.getItem(i);

            //agregar el codigo de identificacion
            Formatter codIdent = new Formatter();
            //codIdent.format("%02d", codigosIdentEncuestados.get(i));
            //respuestaAbierta = "[" + codIdent + "]";
            respuestaUnica = respuestaUnica + "[" + "]";

            if (tipoPreguntaUnicaItem.getPos() == 0)
                respuestaUnica = respuestaUnica + "null";
            else
                respuestaUnica = respuestaUnica + tipoPreguntaUnicaItem.getRespuesta();

            if (i != TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size() - 1)
                respuestaUnica = "" + respuestaUnica + "&";
        }
        System.out.println("Respuesta tipo pregunta unica: {" + respuestaUnica + "}");
    }

    private void leerRespuestasTipoMultiple() {
        String respuestaMultiple = "";
        ArrayList<TipoPreguntaMultipleItem> tipoPreguntaMultipleItems = TipoPreguntaMultipleAdapter.myListPreguntaMultiple;
        int cont = 0;
        //agregar el codigo de identificacion
        Formatter codIdent = new Formatter();

        System.out.println("TipoPreguntaMultipleAdapter.myListPreguntaMultiple" + TipoPreguntaMultipleAdapter.myListPreguntaMultiple.size());

        for (int i = 0; i < tipoPreguntaMultipleItems.size(); i++) {
            //codIdent.format("%02d", codigosIdentEncuestados.get(i));
            //respuestaAbierta = "[" + codIdent + "]";
            respuestaMultiple = respuestaMultiple + "[" + "]";

            for (int j = 0; j < tipoPreguntaMultipleItems.get(i).getRespuestas().size(); j++) {
                System.out.println("multiple usuario " + (i + 1) + ": " + tipoPreguntaMultipleItems.get(i).getRespuestas().get(j));
                respuestaMultiple = respuestaMultiple + tipoPreguntaMultipleItems.get(i).getRespuestas().get(j).toString().trim();

                if (j != tipoPreguntaMultipleItems.get(i).getRespuestas().size() - 1)
                    respuestaMultiple = "" + respuestaMultiple + "$";
                cont++;
            }
            if (i != tipoPreguntaMultipleItems.size() - 1) {
                if (cont == 0)
                    respuestaMultiple = respuestaMultiple + "null";
                respuestaMultiple = "" + respuestaMultiple + "&";
            }
            cont = 0;
        }
        //guardar en base de datos
        System.out.println("respuestaMultiple:{" + respuestaMultiple + "}");
    }

    private void leerRespuestasTipoMixta() {

    }


    private void poblarLista_TipoPreguntaAbierta() {

        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = new TipoPreguntaAbiertaItem();
            tipoPreguntaAbiertaItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaAbiertaItem.setDescription(nombresEncuestados.get(i));

            TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.add(tipoPreguntaAbiertaItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        System.out.println("miListaTipoPreguntaAbierta: " + TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size());

        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaAbiertaAdapter(context, TipoPreguntaAbiertaAdapter.myListPreguntaAbierta));
        System.out.println("num pregunta: " + numPregunta);
        //tipoPreguntaAbiertaAdapter.limpiarLista();
    }

    private void poblarLista_TipoPreguntaUnica() {

        HashMap<Integer, String> alternativas = new HashMap<>();

        alternativas.put(0, "Seleccione alternativa");
        for (int i = 1; i <= 5; i++) {
            alternativas.put(i, "Opcion " + i);
        }

        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaUnicaItem tipoPreguntaUnicaItem = new TipoPreguntaUnicaItem();
            tipoPreguntaUnicaItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaUnicaItem.setAlternativas(alternativas);
            TipoPreguntaUnicaAdapter.myListPreguntaUnica.add(tipoPreguntaUnicaItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaUnicaAdapter(context, TipoPreguntaUnicaAdapter.myListPreguntaUnica));
    }

    private void poblarLista_TipoPreguntaMultiple() {

        ArrayList<String> alternativas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            alternativas.add("Opcion " + (i + 1));
        }

        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaMultipleItem tipoPreguntaMultipleItem = new TipoPreguntaMultipleItem();
            tipoPreguntaMultipleItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaMultipleItem.setAlternativas(alternativas);
            TipoPreguntaMultipleAdapter.myListPreguntaMultiple.add(tipoPreguntaMultipleItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMultipleAdapter.myListPreguntaMultiple.size());
        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMultipleAdapter(context, TipoPreguntaMultipleAdapter.myListPreguntaMultiple));

    }

    private void poblarLista_TipoPreguntaMixta() {
        mumMaxChequeados = 3;

        ArrayList<String> alternativas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            alternativas.add("Opcion " + (i + 1));
        }

        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaMixtaItem tipoPreguntaMixtaItem = new TipoPreguntaMixtaItem();
            tipoPreguntaMixtaItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaMixtaItem.setAlternativas(alternativas);
            TipoPreguntaMixtaAdapter.myListPreguntaMixta.add(tipoPreguntaMixtaItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMixtaAdapter.myListPreguntaMixta.size());

        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMixtaAdapter(context, TipoPreguntaMixtaAdapter.myListPreguntaMixta,
                mumMaxChequeados, ordenImportancia));

    }

    private void poblarLista_TipoPreguntaMatrizSimple() {
        ArrayList<String> horizontales = new ArrayList<>();
        horizontales.add("Pesimo");
        horizontales.add("Malo");
        horizontales.add("Regular");
        horizontales.add("Bueno");
        horizontales.add("Muy Bueno");
        horizontales.add("Excelente");

        ArrayList<String> verticales = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            verticales.add("Opcion " + (i + 1));
        }
        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaMatrizItem tipoPreguntaMatrizItem = new TipoPreguntaMatrizItem();
            tipoPreguntaMatrizItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaMatrizItem.setHorizontal(horizontales);
            tipoPreguntaMatrizItem.setVertical(verticales);
            TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        System.out.println("miListaTipoPreguntaMatrizSimple: " + TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.size());
        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizSimpleAdapter(context, TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz));
        System.out.println("num pregunta: " + numPregunta);
    }

    private void poblarLista_TipoPreguntaMatrizMultiple() {
        ArrayList<String> horizontales = new ArrayList<>();
        horizontales.add("Pesimo");
        horizontales.add("Malo");
        horizontales.add("Regular");
        horizontales.add("Bueno");
        horizontales.add("Muy Bueno");
        horizontales.add("Excelente");

        ArrayList<String> verticales = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            verticales.add("Opcion " + (i + 1));
        }
        //cargar datos a la lista
        for (int i = 0; i < nombresEncuestados.size(); i++) {
            TipoPreguntaMatrizItem tipoPreguntaMatrizItem = new TipoPreguntaMatrizItem();
            tipoPreguntaMatrizItem.setTitle(nombresEncuestados.get(i));
            tipoPreguntaMatrizItem.setHorizontal(horizontales);
            tipoPreguntaMatrizItem.setVertical(verticales);
            TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizItem);
            if (i == 0 && encuestarTodos == false)
                break;
        }
        System.out.println("miListaTipoPreguntaMatrizMultiple: " + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
        lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizMultipleAdapter(context, TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz));
        System.out.println("num pregunta: " + numPregunta);
    }

    private void guardarRespuesta(String rpta) {

    }
}
