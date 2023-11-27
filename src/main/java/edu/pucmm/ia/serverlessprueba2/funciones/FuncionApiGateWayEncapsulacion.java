package edu.pucmm.ia.ds.funciones;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilizando la Interfaz propia de la librería para aws-lambda-java-events, ver en
 * https://github.com/aws/aws-lambda-java-libs
 *
 * Cuando elegir API Gateway Rest o HTTP:
 * https://docs.aws.amazon.com/es_es/apigateway/latest/developerguide/http-api-vs-rest.html
 */
public class FuncionApiGateWayEncapsulacion {

    /**
     * Las clases utilizadas corresponde a la implementación en la librería del SDK.
     * Nos ayudan a trabajar de una manera más rapida. Trabaja para la versión Api gateway HTTP
     * @param request
     * Encapsulación para la versión API Gateway HTTP.
     * @param context
     * @return
     * Encapsulación para la versión API Gateway HTTP.
     * @throws IOException
     */
    public APIGatewayV2HTTPResponse manejadorApiHttp(APIGatewayV2HTTPEvent request, Context context)
            throws IOException {
        //Recuperando la entrada, ver en CloudWatch la salida:
        context.getLogger().log("Petición: "+new Gson().toJson(request));
        //context.getLogger().log("Metodo de acceso: "+request.getRequestContext().getHttp().getMethod());
        context.getLogger().log("Parametros enviados: "+request.getRawQueryString());
        context.getLogger().log("Cuerpo enviado: "+request.getBody());
        //Salida.
        APIGatewayV2HTTPResponse reponse = new APIGatewayV2HTTPResponse();
        reponse.setStatusCode(200);
        reponse.setBody("Hola Mundo Serverless Utilizando APIGateway tipo HTTP vía  :-D");
        return reponse;
    }


    /**
     * Las clases utilizadas corresponde a la implementación en la librería del SDK.
     * Nos ayudan a trabajar de una manera más rápida. Trabaja para la versión Api gateway Rest.
     * Para utilizar en AWS cargar: edu.pucmm.ia.ds.funciones.FuncionApiGateWayEncapsulacion::manejadorApiRest
     * @param request
     * @param context
     * @return
     * @throws IOException
     */
    public APIGatewayProxyResponseEvent manejadorApiRest(APIGatewayProxyRequestEvent request, Context context)
            throws IOException {
        //Recuperando la entrada, ver en CloudWatch la salida:
        context.getLogger().log("Metodo de acceso: "+request.getHttpMethod());
        context.getLogger().log("Cuerpo enviado: "+request.getBody());
        //Salida.
        APIGatewayProxyResponseEvent reponse = new APIGatewayProxyResponseEvent();
        reponse.setStatusCode(200);
        //Habilitando el CORS
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        reponse.setHeaders(headers);
        //
        reponse.setBody("Hola Mundo Serverless Utilizando APIGateway tipo REST vía  :-D -> Información Recibido: "+request.toString());
        return reponse;
    }


}
