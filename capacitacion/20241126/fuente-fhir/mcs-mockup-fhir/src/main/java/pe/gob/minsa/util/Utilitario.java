package pe.gob.minsa.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utilitario {

	public static String getTipoDocumento(String tipoDocumento) {

		String descr = null;

		switch (tipoDocumento) {
		case "1":
			descr = "DNI";
			break;
		case "2":
			descr = "Carné extranjería";
			break;
		case "3":
			descr = "Pasaporte";
			break;
		case "4":
			descr = "Documento de identidad extranjero";
			break;
		default:
			descr = "Sin tipo de documento";
			break;
		}

		return descr;

	}

	public static String getNomenglaturaPais(String nombrePais) {

		String descr = null;

		String cadenaNormalize = Normalizer.normalize(nombrePais, Normalizer.Form.NFD);
		String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "").toUpperCase();

		switch (cadenaSinAcentos) {
		case "AFGANISTAN":
			descr = "AFG";
			break;
		case "ALBANIA":
			descr = "ALB";
			break;
		case "ALEMANIA":
			descr = "DEU";
			break;
		case "ANDORRA":
			descr = "AND";
			break;
		case "ANGOLA":
			descr = "AGO";
			break;
		case "ANGUILA":
			descr = "AIA";
			break;
		case "ANTARTIDA":
			descr = "ATA";
			break;
		case "ANTIGUA Y BARBUDA":
			descr = "ATG";
			break;
		case "ARABIA SAUDITA":
			descr = "SAU";
			break;
		case "ARGELIA":
			descr = "DZA";
			break;
		case "ARGENTINA":
			descr = "ARG";
			break;
		case "ARMENIA":
			descr = "ARM";
			break;
		case "ARUBA":
			descr = "ABW";
			break;
		case "AUSTRALIA":
			descr = "AUS";
			break;
		case "AUSTRIA":
			descr = "AUT";
			break;
		case "AZERBAIYAN":
			descr = "AZE";
			break;
		case "BELGICA":
			descr = "BEL";
			break;
		case "BAHAMAS":
			descr = "BHS";
			break;
		case "BAHREIN":
			descr = "BHR";
			break;
		case "BANGLADESH":
			descr = "BGD";
			break;
		case "BARBADOS":
			descr = "BRB";
			break;
		case "BELICE":
			descr = "BLZ";
			break;
		case "BENIN":
			descr = "BEN";
			break;
		case "BUTAN":
			descr = "BTN";
			break;
		case "BIELORRUSIA":
			descr = "BLR";
			break;
		case "BIRMANIA":
			descr = "MMR";
			break;
		case "BOLIVIA":
			descr = "BOL";
			break;
		case "BOSNIA Y HERZEGOVINA":
			descr = "BIH";
			break;
		case "BOTSWANA":
			descr = "BWA";
			break;
		case "BRASIL":
			descr = "BRA";
			break;
		case "BRUNEI":
			descr = "BRN";
			break;
		case "BULGARIA":
			descr = "BGR";
			break;
		case "BURKINA FASO":
			descr = "BFA";
			break;
		case "BURUNDI":
			descr = "BDI";
			break;
		case "CABOVERDE":
			descr = "CPV";
			break;
		case "CAMBOYA":
			descr = "KHM";
			break;
		case "CAMERUN":
			descr = "CMR";
			break;
		case "CANADA":
			descr = "CAN";
			break;
		case "CHAD":
			descr = "TCD";
			break;
		case "CHILE":
			descr = "CHL";
			break;
		case "CHINA":
			descr = "CHN";
			break;
		case "CHIPRE":
			descr = "CYP";
			break;
		case "CIUDAD DEL VATICANO":
			descr = "VAT";
			break;
		case "COLOMBIA":
			descr = "COL";
			break;
		case "COMORAS":
			descr = "COM";
			break;
		case "REPUBLICA DEL CONGO":
			descr = "COG";
			break;
		case "REPUBLICA DEMOCRATICA DEL CONGO":
			descr = "COD";
			break;
		case "COREA DEL NORTE":
			descr = "PRK";
			break;
		case "COREA DEL SUR":
			descr = "KOR";
			break;
		case "COSTA DE MARFIL":
			descr = "CIV";
			break;
		case "COSTA RICA":
			descr = "CRI";
			break;
		case "CROACIA":
			descr = "HRV";
			break;
		case "CUBA":
			descr = "CUB";
			break;
		case "CURAZAO":
			descr = "CWU";
			break;
		case "DINAMARCA":
			descr = "DNK";
			break;
		case "DOMINICA":
			descr = "DMA";
			break;
		case "ECUADOR":
			descr = "ECU";
			break;
		case "EGIPTO":
			descr = "EGY";
			break;
		case "EL SALVADOR":
			descr = "SLV";
			break;
		case "EMIRATOS ARABES UNIDOS":
			descr = "ARE";
			break;
		case "ERITREA":
			descr = "ERI";
			break;
		case "ESLOVAQUIA":
			descr = "SVK";
			break;
		case "ESLOVENIA":
			descr = "SVN";
			break;
		case "ESPAÑA":
			descr = "ESP";
			break;
		case "ESTADOS UNIDOS DE AMERICA":
			descr = "USA";
			break;
		case "ESTONIA":
			descr = "EST";
			break;
		case "ETIOPIA":
			descr = "ETH";
			break;
		case "FILIPINAS":
			descr = "PHL";
			break;
		case "FINLANDIA":
			descr = "FIN";
			break;
		case "FIYI":
			descr = "FJI";
			break;
		case "FRANCIA":
			descr = "FRA";
			break;
		case "GABO":
			descr = "GAB";
			break;
		case "GAMBA":
			descr = "GMB";
			break;
		case "GEORGIA":
			descr = "GEO";
			break;
		case "GHANA":
			descr = "GHA";
			break;
		case "GIBRALTAR":
			descr = "GIB";
			break;
		case "GRANADA":
			descr = "GRD";
			break;
		case "GRECIA":
			descr = "GRC";
			break;
		case "GROENLANDIA":
			descr = "GRL";
			break;
		case "GUADALUPE":
			descr = "GLP";
			break;
		case "GUAM":
			descr = "GUM";
			break;
		case "GUATEMALA":
			descr = "GTM";
			break;
		case "GUAYANA FRANCESA":
			descr = "GUF";
			break;
		case "GUERNSEY":
			descr = "GGY";
			break;
		case "GUINEA":
			descr = "GIN";
			break;
		case "GUINEA ECUATORIAL":
			descr = "GNQ";
			break;
		case "GUINEA-BISSAU":
			descr = "GNB";
			break;
		case "GUYANA":
			descr = "GUY";
			break;
		case "HAITI":
			descr = "HTI";
			break;
		case "HONDURAS":
			descr = "HND";
			break;
		case "HONGKONG":
			descr = "HKG";
			break;
		case "HUNGRIA":
			descr = "HUN";
			break;
		case "INDIA":
			descr = "IND";
			break;
		case "INDONESIA":
			descr = "IDN";
			break;
		case "IRAN":
			descr = "IRN";
			break;
		case "IRAK":
			descr = "IRQ";
			break;
		case "IRLANDA":
			descr = "IRL";
			break;
		case "ISLABOUVET":
			descr = "BVT";
			break;
		case "ISLA DE MAN":
			descr = "IMN";
			break;
		case "ISLA DE NAVIDAD":
			descr = "CXR";
			break;
		case "ISLA NORFOLK":
			descr = "NFK";
			break;
		case "ISLANDIA":
			descr = "ISL";
			break;
		case "ISLAS BERMUDAS":
			descr = "BMU";
			break;
		case "ISLAS CAIMAN":
			descr = "CYM";
			break;
		case "ISLAS COCOS":
			descr = "CCK";
			break;
		case "ISLAS COOK":
			descr = "COK";
			break;
		case "ISLAS DE ALAND":
			descr = "ALA";
			break;
		case "ISLAS FEROE":
			descr = "FRO";
			break;
		case "ISLAS GEORGIAS DEL SUR Y SANDWICH DEL SUR":
			descr = "SGS";
			break;
		case "ISLAS HEARD Y MCDONALD":
			descr = "HMD";
			break;
		case "ISLAS MALDIVAS":
			descr = "MDV";
			break;
		case "ISLAS MALVINAS":
			descr = "FLK";
			break;
		case "ISLAS MARIANAS DEL NORTE":
			descr = "MNP";
			break;
		case "ISLAS MARSHALL":
			descr = "MHL";
			break;
		case "ISLAS PITCAIRN":
			descr = "PCN";
			break;
		case "ISLAS SALOMON":
			descr = "SLB";
			break;
		case "ISLAS TURCAS Y CAICOS":
			descr = "TCA";
			break;
		case "ISLAS ULTRAMARINAS MENORES DE ESTADOS UNIDOS":
			descr = "UMI";
			break;
		case "ISLAS VIRGENES BRITANICAS":
			descr = "VGB";
			break;
		case "ISLAS VIRGENES DE LOS ESTADOS UNIDOS":
			descr = "VIR";
			break;
		case "ISRAEL":
			descr = "ISR";
			break;
		case "ITALIA":
			descr = "ITA";
			break;
		case "JAMAICA":
			descr = "JAM";
			break;
		case "JAPON":
			descr = "JPN";
			break;
		case "JERSEY":
			descr = "JEY";
			break;
		case "JORDANIA":
			descr = "JOR";
			break;
		case "KAZAJISTAN":
			descr = "KAZ";
			break;
		case "KENIA":
			descr = "KEN";
			break;
		case "KIRGUISTAN":
			descr = "KGZ";
			break;
		case "KIRIBATI":
			descr = "KIR";
			break;
		case "KUWAIT":
			descr = "KWT";
			break;
		case "LIBANO":
			descr = "LBN";
			break;
		case "LAOS":
			descr = "LAO";
			break;
		case "LESOTO":
			descr = "LSO";
			break;
		case "LETONIA":
			descr = "LVA";
			break;
		case "LIBERIA":
			descr = "LBR";
			break;
		case "LIBIA":
			descr = "LBY";
			break;
		case "LIECHTENSTEIN":
			descr = "LIE";
			break;
		case "LITUANIA":
			descr = "LTU";
			break;
		case "LUXEMBURGO":
			descr = "LUX";
			break;
		case "MEXICO":
			descr = "MEX";
			break;
		case "MONACO":
			descr = "MCO";
			break;
		case "MACA":
			descr = "MAC";
			break;
		case "MACEDONIA":
			descr = "MKD";
			break;
		case "MADAGASCAR":
			descr = "MDG";
			break;
		case "MALASIA":
			descr = "MYS";
			break;
		case "MALAWI":
			descr = "MWI";
			break;
		case "MALI":
			descr = "MLI";
			break;
		case "MALTA":
			descr = "MLT";
			break;
		case "MARRUECOS":
			descr = "MAR";
			break;
		case "MARTINICA":
			descr = "MTQ";
			break;
		case "MAURICIO":
			descr = "MUS";
			break;
		case "MAURITANIA":
			descr = "MRT";
			break;
		case "MAYOTTE":
			descr = "MYT";
			break;
		case "MICRONESIA":
			descr = "FSM";
			break;
		case "MOLDAVIA":
			descr = "MDA";
			break;
		case "MONGOLIA":
			descr = "MNG";
			break;
		case "MONTENEGRO":
			descr = "MNE";
			break;
		case "MONTSERRAT":
			descr = "MSR";
			break;
		case "MOZAMBIQUE":
			descr = "MOZ";
			break;
		case "NAMIBIA":
			descr = "NAM";
			break;
		case "NAURU":
			descr = "NRU";
			break;
		case "NEPAL":
			descr = "NPL";
			break;
		case "NICARAGUA":
			descr = "NIC";
			break;
		case "NIGER":
			descr = "NER";
			break;
		case "NIGERIA":
			descr = "NGA";
			break;
		case "NIUE":
			descr = "NIU";
			break;
		case "NORUEGA":
			descr = "NOR";
			break;
		case "NUEVA CALEDONIA":
			descr = "NCL";
			break;
		case "NUEVA ZELANDA":
			descr = "NZL";
			break;
		case "OMAN":
			descr = "OMN";
			break;
		case "PAISES BAJOS":
			descr = "NLD";
			break;
		case "PAKISTAN":
			descr = "PAK";
			break;
		case "PALA":
			descr = "PLW";
			break;
		case "PALESTINA":
			descr = "PSE";
			break;
		case "PANAMA":
			descr = "PAN";
			break;
		case "PAPUA NUEVA GUINEA":
			descr = "PNG";
			break;
		case "PARAGUAY":
			descr = "PRY";
			break;
		case "PERU":
			descr = "PER";
			break;
		case "POLINESIA FRANCESA":
			descr = "PYF";
			break;
		case "POLONIA":
			descr = "POL";
			break;
		case "PORTUGAL":
			descr = "PRT";
			break;
		case "PUERTO RICO":
			descr = "PRI";
			break;
		case "QATAR":
			descr = "QAT";
			break;
		case "REINO UNIDO":
			descr = "GBR";
			break;
		case "REPUBLICA CENTROAFRICANA":
			descr = "CAF";
			break;
		case "REPUBLICA CHECA":
			descr = "CZE";
			break;
		case "REPUBLICA DOMINICANA":
			descr = "DOM";
			break;
		case "REPUBLICA DE SUDAN DEL SUR":
			descr = "SSD";
			break;
		case "REUNION":
			descr = "REU";
			break;
		case "RUANDA":
			descr = "RWA";
			break;
		case "RUMANIA":
			descr = "ROU";
			break;
		case "RUSIA":
			descr = "RUS";
			break;
		case "SAHARA OCCIDENTAL":
			descr = "ESH";
			break;
		case "SAMOA":
			descr = "WSM";
			break;
		case "SAMOA AMERICANA":
			descr = "ASM";
			break;
		case "SAN BARTOLOME":
			descr = "BLM";
			break;
		case "SAN CRISTOBAL Y NIEVES":
			descr = "KNA";
			break;
		case "SAN MARINO":
			descr = "SMR";
			break;
		case "SAN MARTIN":
			descr = "MAF";
			break;
		case "SAN PEDRO Y MIQUELON":
			descr = "SPM";
			break;
		case "SAN VICENTE Y LAS GRANADINAS":
			descr = "VCT";
			break;
		case "SANTA ELENA":
			descr = "SHN";
			break;
		case "SANTA LUCIA":
			descr = "LCA";
			break;
		case "SANTA TOME Y PRINCIPE":
			descr = "STP";
			break;
		case "SENEGAL":
			descr = "SEN";
			break;
		case "SERBIA":
			descr = "SRB";
			break;
		case "SEYCHELLES":
			descr = "SYC";
			break;
		case "SIERRA LEONA":
			descr = "SLE";
			break;
		case "SINGAPUR":
			descr = "SGP";
			break;
		case "SINT MAARTEN":
			descr = "SMX";
			break;
		case "SIRI":
			descr = "SYR";
			break;
		case "SOMALIA":
			descr = "SOM";
			break;
		case "SRI LANKA":
			descr = "LKA";
			break;
		case "SUDAFRICA":
			descr = "ZAF";
			break;
		case "SUDAN":
			descr = "SDN";
			break;
		case "SUECIA":
			descr = "SWE";
			break;
		case "SUIZA":
			descr = "CHE";
			break;
		case "SURINAM":
			descr = "SUR";
			break;
		case "SVALARD Y JAN MAYEN":
			descr = "SJM";
			break;
		case "SUAZILANDIA":
			descr = "SWZ";
			break;
		case "TAYIKISTAN":
			descr = "TJK";
			break;
		case "TAILANDIA":
			descr = "THA";
			break;
		case "TAIWAN":
			descr = "TWN";
			break;
		case "TANZANIA":
			descr = "TZA";
			break;
		case "TERRITORIO BRITANICO DEL OCEANO INDICO":
			descr = "IOT";
			break;
		case "TERRITORIOS AUSTRALES Y ANTARTICAS FRANCESES":
			descr = "ATF";
			break;
		case "TIMOR ORIENTAL":
			descr = "TLS";
			break;
		case "TOGO":
			descr = "TGO";
			break;
		case "TOKELAU":
			descr = "TKL";
			break;
		case "TONGA":
			descr = "TON";
			break;
		case "TRINIDAD Y TOBAGO":
			descr = "TTO";
			break;
		case "TUNEZ":
			descr = "TUN";
			break;
		case "TURKMENISTAN":
			descr = "TKM";
			break;
		case "TURQUIA":
			descr = "TUR";
			break;
		case "TUVALU":
			descr = "TUV";
			break;
		case "UCRANIA":
			descr = "UKR";
			break;
		case "UGANDA":
			descr = "UGA";
			break;
		case "URUGUAY":
			descr = "URY";
			break;
		case "UZBEKISTAN":
			descr = "UZB";
			break;
		case "VANUATU":
			descr = "VUT";
			break;
		case "VENEZUELA":
			descr = "VEN";
			break;
		case "VIETNAM":
			descr = "VNM";
			break;
		case "WALLIS Y FUTUNA":
			descr = "WLF";
			break;
		case "YEMEN":
			descr = "YEM";
			break;
		case "YIBUTI":
			descr = "DJI";
			break;
		case "ZAMBIA":
			descr = "ZMB";
			break;
		case "ZIMBABWE":
			descr = "ZWE";
			break;

		default:
			descr = "PSN";
			break;
		}

		return descr;

	}
	
	public static String validateActivo(String activo) {		
		if(Pattern.compile("^(true|false)$").matcher(activo).matches()) {
			return "0000";
		}else
			return "6000";	
				
	}
	
	public static String validateTipoDocumento(String tipoDocumento) {		
		if(Pattern.compile("^([1]|[2]|[3]|[4]){1}$").matcher(tipoDocumento).matches()) {
			return "0000";
		}else
			return "6000";	
				
	}
	
	public static String validateNumero(String dni) {		
		if(Pattern.compile("^(\\d*)$").matcher(dni).matches()) {
			return "0000";
		}else
			return "6000";	
				
	}
	
	public static String validateFechaNacimiento(String fecha) {		
		if(Pattern.compile("^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(?:(?:(-)(?:0?[13578]|1[02])\\1(?:31))|(?:(-)(?:0?[13-9]|1[0-2])\\2(?:29|30)))$|^(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)0?2\\3(?:29)$|^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:0[1-9]|1\\d|2[0-8])$").matcher(fecha).matches()) {
			return "0000";
		}else
			return "6000";	
				
	}
	
	public static String validateGenero(String sexo) {		
		if(Pattern.compile("^([M,F]|[m,f]){1}$").matcher(sexo).matches()) {
			return "0000";
		}else
			return "6000";	
				
	}

}
