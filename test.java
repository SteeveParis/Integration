public class Main {

	public static void main(String... args) throws Exception {
		ApplicationContext ctx =
			new ClassPathXmlApplicationContext("context.xml");
		// Simple Service
		TempConverter converter =
			ctx.getBean("simpleGateway", TempConverter.class);
		System.out.println(converter.fahrenheitToCelcius(68.0f));
		// Web Service
		converter  = ctx.getBean("wsGateway", TempConverter.class);
		System.out.println(converter.fahrenheitToCelcius(68.0f));
	}
}
public interface TempConverter {

	float fahrenheitToCelcius(float fahren);

}

<int:gateway id="simpleGateway"
	service-interface="foo.TempConverter"
	default-request-channel="simpleExpression" />

<int:service-activator id="expressionConverter"
	input-channel="simpleExpression"
	expression="(payload - 32) / 9 * 5"/>

<int:gateway id="wsGateway" service-interface="foo.TempConverter"
	default-request-channel="viaWebService" />

<int:chain id="wsChain" input-channel="viaWebService">
	<int:transformer
	   expression="'&lt;FahrenheitToCelsius xmlns=&quot;https://www.w3schools.com/xml/&quot;&gt;&lt;Fahrenheit&gt;XXX&lt;/Fahrenheit&gt;&lt;/FahrenheitToCelsius&gt;'.replace('XXX', payload.toString())" />
	<int-ws:header-enricher>
		<int-ws:soap-action value="https://www.w3schools.com/xml/FahrenheitToCelsius"/>
	</int-ws:header-enricher>
	<int-ws:outbound-gateway
		uri="https://www.w3schools.com/xml/tempconvert.asmx"/>
	<int-xml:xpath-transformer
		xpath-expression="/*[local-name()='FahrenheitToCelsiusResponse']/*[local-name()='FahrenheitToCelsiusResult']"/>
</int:chain>

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
    TempConverter converter = ctx.getBean(TempConverter.class);
    System.out.println(converter.fahrenheitToCelcius(68.0f));
    ctx.close();
  }

  @MessagingGateway
  public interface TempConverter {

    @Gateway(requestChannel = "convert.input")
    float fahrenheitToCelcius(float fahren);

  }

  @Bean
  public IntegrationFlow convert() {
      return f -> f
        .transform(payload ->
              "<FahrenheitToCelsius xmlns=\"https://www.w3schools.com/xml/\">"
            +     "<Fahrenheit>" + payload + "</Fahrenheit>"
            + "</FahrenheitToCelsius>")
        .enrichHeaders(h -> h
            .header(WebServiceHeaders.SOAP_ACTION,
                "https://www.w3schools.com/xml/FahrenheitToCelsius"))
        .handle(new SimpleWebServiceOutboundGateway(
            "https://www.w3schools.com/xml/tempconvert.asmx"))
        .transform(Transformers.xpath("/*[local-name()=\"FahrenheitToCelsiusResponse\"]"
            + "/*[local-name()=\"FahrenheitToCelsiusResult\"]"));
  }

}
