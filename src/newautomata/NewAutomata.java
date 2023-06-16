package newautomata;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
public class NewAutomata {
    public static void main(String[] args) {
        AnalisisLexico();
    }
    public static File texto;
    public static boolean fallo;
    public static String combierte="";
    public static String LeerBuf, guarda = "", extrae = "",extrae2="";
    public static char quitar,quitar2;
    public static int contFil = 0;
    public static Pattern patron;
    public static Matcher maches;
    public static String SimbEspecial = "([\\(]|[\\)]|[?]|[¿]|[+]|[-]|[=]|[*]|[/]|[&]|[\\|]|[;]|[\\{]|[\\}]|[,]|[<]|[>])";
    public static String caracter = "([A-Z]|[0-9]|[\\.])";
    public static String espacio = "[\\s]";
    public static String comillas = "[\"]";
    public static boolean ComillaAbierta = false;
    public static String reservado = "(IMPRIMIR|LEER|FIN|TRUE|FALSE|IF|ELSE|FOR|WHILE|INICIO|INT|FLOAT|CADE|BOOLEAN)";
    public static List <String> IDTabla=new ArrayList<>();
    public static List <String> TipoTabla=new ArrayList<>();
    public static List <String> ValorTabla=new ArrayList<>();
    public static List <String> GuardaLinea=new ArrayList<>();
    public static List <String> Errores=new ArrayList<>();
    public static List <String> ErrorFila=new ArrayList<>();
    public static List <String> TextError=new ArrayList<>();
    public static void AnalisisLexico() {
        try {
            texto = new File("Errores.txt");
            BufferedReader bt = new BufferedReader(new FileReader(texto));
            while((LeerBuf = bt.readLine()) != null){
                guarda = guarda + LeerBuf;
                TextError.add(guarda);
                guarda="";
            }
            texto = new File("Automata.txt");
            BufferedReader bf = new BufferedReader(new FileReader(texto));
            while ((LeerBuf = bf.readLine()) != null) {
                contFil++;
                guarda = guarda + LeerBuf;
                for (int i = 0; i < guarda.length(); i++) {
                    quitar = guarda.charAt(i);
                    extrae = extrae + quitar;
                    patron = Pattern.compile(caracter);
                    maches = patron.matcher(extrae);
                    if (!maches.matches()) {
                        patron = Pattern.compile(SimbEspecial);
                        maches = patron.matcher(extrae);
                        if (!maches.matches()) {
                            patron = Pattern.compile(espacio);
                            maches = patron.matcher(extrae);
                            if (!maches.matches()) {
                                patron = Pattern.compile(comillas);
                                maches = patron.matcher(extrae);
                                if (!maches.matches()) {
                                    fallo=true;
                                    Errores.add("001");
                                    combierte=combierte+contFil;
                                    ErrorFila.add(combierte);
                                    combierte="";
                                }
                            }
                        }else{
                            if (extrae.equals("{")) {
                                Errores.add("006");
                                combierte=combierte+contFil;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                            if (extrae.equals("}")) {
                                if (!Errores.isEmpty()) {
                                    for (int j = Errores.size()-1; j >= 0; j--) {
                                        if ("006".equals(Errores.get(j))) {
                                            Errores.remove(j);
                                            ErrorFila.remove(j);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    extrae = "";
                }
                guarda = "";
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error");
        }
        if (fallo==false) {
            analizadorSint();
        }
        else{
            System.out.println("\nTabla de simbolos");
            for (int i = 0; i < IDTabla.size(); i++) {
                System.out.println(IDTabla.get(i)+", "+TipoTabla.get(i)+", "+ValorTabla.get(i));
            }
            System.out.println("\nTabla de errores");
            int r;
            for (int i = 0; i < Errores.size(); i++) {
                System.out.println("Codigo de error: "+Errores.get(i)+", Fila: "+ErrorFila.get(i)+", ");
                r=Integer.parseInt(Errores.get(i));
                System.out.println(TextError.get(r-1));
            }
        }
    }
    public static String entero = "[0-9]+";
    public static String operador = "((=)|(>=)|(<=)|(<)|(>)|(<>))";
    public static String operadorLogic = "((&&)|[\\|])";
    public static String operadorArit = "([\\+]|[\\-]|[\\/]|[\\*])";
    public static String Flotante = "[0-9]+[\\.][0-9]+";
    public static String boleano = "(TRUE|FALSE)";
    public static String cadena = "\".*\"";
    public static String ID = "[A-Z][A-Z0-9]*";
    public static String operacion = "[\\s]*" + ID + "[\\s]*=[\\s]*(((" + entero + "|" + Flotante + "|" + ID + ")([\\s]*" + operadorArit + "[\\s]*(" + entero + "|" + Flotante + "|" + ID + "))?)||(("+ID+"||" + cadena +")([\\s]*[+][\\s]*("+ID+"||" + cadena +"))?))[\\s]*[\\;][\\s]*";
    public static String operacion2 = "[\\s]*" + ID + "[\\s]*=[\\s]*(" + cadena + "|" + boleano + ")[\\s]*[\\;][\\s]*";
    public static String VarInt = "[\\s]*(INT)[\\s]+" + ID + "([\\s]*=[\\s]*(" + entero +"|"+ID+"))?[\\s]*[\\;][\\s]*";
    public static String VarFlotante = "[\\s]*(FLOAT)[\\s]+" + ID + "([\\s]*=[\\s]*("+ Flotante+ "|" + ID +"))?[\\s]*[\\;][\\s]*";
    public static String VarCade = "[\\s]*(CADE)[\\s]+" + ID + "([\\s]*=[\\s]*(" + cadena + "|" + ID +"))?[\\s]*[\\;][\\s]*";
    public static String VarBoolean = "[\\s]*(BOOLEAN)[\\s]+" + ID + "[\\s]*=[\\s]*" + boleano + "[\\s]*[\\;][\\s]*";
    public static String inicio = "(INICIO)[\\s]+" + ID + "[\\s]*";
    public static String ProgFin = "[\\s]*(FIN)[\\s]*";
    public static String VarLeer = "[\\s]*(LEER)[\\s]+" + ID + "[\\s]*[\\;][\\s]*";
    public static String VarImprimir = "[\\s]*(IMPRIMIR)[\\s]+("+cadena+ "|" + ID + "|" + entero + "|" + Flotante + "|" + boleano + ")[\\s]*[\\;][\\s]*";
    public static String sicloFor ="[\\s]*(FOR)[\\s]*[\\(][\\s]*" + ID + "[\\s]*[\\;][\\s]*(("+entero+")|(" + ID + "([\\s]*[+][\\s]*(" + entero + "|" + ID + "))?))[\\s]*[\\;][\\s]*((" + entero + ")|(" + ID + "([\\s]*[+][\\s]*(" + entero + "|" + ID + "))?))[\\s]*[\\)][\\s]*[\\{][\\s]*";
    public static String DeclaracionIF = "[\\s]*(IF)[\\s]*[\\(][\\s]*(((" + entero + "|" + Flotante + "|" + ID + ")[\\s]*" + operador + "[\\s]*(" + entero + "|" + Flotante + "|" + ID + "))|(("+ ID + "|" + boleano + "|" + cadena+")[\\s]*=[\\s]*(" + cadena + "|" + boleano + "|" + ID + ")))[\\s]*[\\)][\\s]*[\\{][\\s]*";
    public static String sicloWhile = "[\\s]*(WHILE)[\\s]*[\\(][\\s]*" + ID + "[\\s]*" + operador + "[\\s]*(" + entero + "|" + cadena + "|" + ID + "|" + Flotante +")([\\s]*[+][\\s]*(" + ID + "|" + entero + "|" + Flotante + "|" + cadena + "))?[\\s]*[\\)][\\s]*[\\{][\\s]*";
    public static String FinDeBloque ="[\\s]*[}][\\s]*";
    public static int contFil2=0; 
    public static int r = 0;
    public static void analizadorSint() {
        texto = new File("Automata.txt");
        if (contFil == 1) {
            Errores.add("004");
            combierte=combierte+contFil;
            ErrorFila.add(combierte);
            combierte="";
        } else {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(texto));
                while ((LeerBuf = bf.readLine()) != null) {
                    fallo=false;
                    contFil2++;
                    guarda = guarda + LeerBuf;
                    if (contFil2== 1) {
                        patron = Pattern.compile(inicio);
                        maches = patron.matcher(guarda);
                        if (!maches.matches()) {
                            Errores.add("005");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                            break;
                        }
                    } else {
                        if (contFil2 == contFil) {
                            patron = Pattern.compile(ProgFin);
                            maches = patron.matcher(guarda);
                            if (!maches.matches()) {
                                Errores.add("006");
                                combierte=combierte+contFil2;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                        } else {
                            patron = Pattern.compile(VarLeer);
                            maches = patron.matcher(guarda);
                            if (!maches.matches()) {
                                patron = Pattern.compile(VarImprimir);
                                maches = patron.matcher(guarda);
                                if (!maches.matches()) {
                                    patron = Pattern.compile(VarInt);
                                    maches = patron.matcher(guarda);
                                    if (!maches.matches()) {
                                        patron = Pattern.compile(VarFlotante);
                                        maches = patron.matcher(guarda);
                                        if (!maches.matches()) {
                                            patron = Pattern.compile(VarBoolean);
                                            maches = patron.matcher(guarda);
                                            if (!maches.matches()) {
                                                patron = Pattern.compile(VarCade);
                                                maches = patron.matcher(guarda);
                                                if (!maches.matches()) {
                                                    patron = Pattern.compile(sicloFor);
                                                    maches = patron.matcher(guarda);
                                                    if (!maches.matches()) {
                                                        patron = Pattern.compile(sicloWhile);
                                                        maches = patron.matcher(guarda);
                                                        if (!maches.matches()) {
                                                            patron = Pattern.compile(DeclaracionIF);
                                                            maches = patron.matcher(guarda);
                                                            if (!maches.matches()) {
                                                                patron = Pattern.compile(operacion);
                                                                maches = patron.matcher(guarda);
                                                                if (!maches.matches()) {
                                                                    patron = Pattern.compile(operacion2);
                                                                    maches = patron.matcher(guarda);
                                                                    if (!maches.matches()) {
                                                                        patron = Pattern.compile(FinDeBloque);
                                                                        maches = patron.matcher(guarda);
                                                                        if (!maches.matches()) {
                                                                            Errores.add("002");
                                                                            combierte=combierte+contFil2;
                                                                            ErrorFila.add(combierte);
                                                                            combierte="";
                                                                            fallo=true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (fallo==false) {
                        System.out.print(guarda);
                        separar();
                        if (GuardaLinea.size()>2) {
                            AnalizarToken();
                        }
                        GuardaLinea.clear();
                    }
                    id="";
                    guarda = "";
                }
            } catch (IOException | HeadlessException e) {
                System.out.println("ocurrio un problema");
            }
        }
        System.out.println("\nTabla de simbolos");
        for (int i = 0; i < IDTabla.size(); i++) {
            System.out.println(IDTabla.get(i)+", "+TipoTabla.get(i)+", "+ValorTabla.get(i));
        }
        System.out.println("\nTabla de errores");
        for (int i = 0; i < Errores.size(); i++) {
            System.out.print("Codigo de error: "+Errores.get(i)+", Fila: "+ErrorFila.get(i)+", ");
            r=Integer.parseInt(Errores.get(i));
            System.out.println(TextError.get(r-1));
        }
    }
    public static String token = "";
    public static void separar() {
        clasificacion(guarda);
        for (int i = 0; i < guarda.length(); i++) {
            quitar = guarda.charAt(i);
            extrae = extrae + quitar;
            patron = Pattern.compile(caracter);
            maches = patron.matcher(extrae);
            if (!maches.matches()) {
                patron = Pattern.compile(SimbEspecial);
                maches = patron.matcher(extrae);
                if (!maches.matches()) {
                    patron = Pattern.compile(espacio);
                    maches = patron.matcher(extrae);
                    if (!maches.matches()) {
                        patron = Pattern.compile(comillas);
                        maches = patron.matcher(extrae);
                        if (maches.matches()) {
                            if (ComillaAbierta == false) {
                                if (token.equals("")) {
                                    token = token + extrae;
                                    ComillaAbierta = true;
                                } else {
                                    GuardaLinea.add(token);
                                    token = "";
                                    token = token + extrae;
                                    ComillaAbierta = true;
                                }
                            } else {
                                token = token + extrae;
                                GuardaLinea.add(token);
                                token = "";
                                ComillaAbierta = false;
                            }
                        }
                    } else {
                        if (ComillaAbierta == false) {
                            if (token.equals("")) {
                            } else {
                                GuardaLinea.add(token);
                                token = "";
                            }
                        } else {
                            token = token + extrae;
                        }
                    }
                } else {
                    if (ComillaAbierta == false) {
                        if (token.equals("")) {
                            token = token + extrae;
                            if ((i)<(guarda.length()-1)) {
                                quitar2=guarda.charAt(i+1);
                                extrae2=extrae2+quitar2;
                                if (("+".equals(extrae)&&"+".equals(extrae2))||("-".equals(extrae)&&"-".equals(extrae2))||("<".equals(extrae)&&">".equals(extrae2))||(">".equals(extrae)&&"=".equals(extrae2))||("<".equals(extrae)&&"=".equals(extrae2))){}
                                else{GuardaLinea.add(token);token = "";}
                                extrae2="";
                            }
                            else{GuardaLinea.add(token);token = "";}
                        } else {
                            if (("+".equals(extrae)&&"+".equals(token))||("-".equals(extrae)&&"-".equals(token))||(">".equals(extrae)&&"<".equals(token))||("=".equals(extrae)&&">".equals(token))||("=".equals(extrae)&&"<".equals(token))){
                                token=token+extrae;
                                GuardaLinea.add(token);
                                token = "";
                            }
                            else{
                                GuardaLinea.add(token);
                                token = "";
                                token = token + extrae;
                                if ((i+1)<=(guarda.length()-1)) {
                                    quitar2=guarda.charAt(i+1);
                                    extrae2=extrae2+quitar2;
                                    if (("+".equals(extrae2)&&"+".equals(extrae))||("-".equals(extrae2)&&"-".equals(extrae))||(">".equals(extrae2)&&"<".equals(extrae))||("=".equals(extrae2)&&">".equals(extrae))||("=".equals(extrae2)&&"<".equals(extrae))){}
                                    else{GuardaLinea.add(token);token = "";}
                                    extrae2="";
                                }else{GuardaLinea.add(token);token = "";}
                            }
                        }
                    } else {
                        token = token + extrae;
                    }
                }
            } else {
                token = token + extrae;
            }
            extrae = "";
        }
        if (!token.equals("")) {
            GuardaLinea.add(token);
            token = "";
        }
    }
    public static String OperacionArit = "("+operacion+"|"+operacion2+")";
    public static String Declaraciones = "("+VarInt+"|"+VarBoolean+"|"+VarCade+"|"+VarFlotante+"|"+inicio+")";
    public static void clasificacion(String g) {
        patron = Pattern.compile(OperacionArit);
        maches = patron.matcher(g);
        if (!maches.matches()) {
            patron = Pattern.compile(Declaraciones);
            maches = patron.matcher(g);
            if (!maches.matches()) {
                patron = Pattern.compile(VarLeer);
                maches = patron.matcher(g);
                if (!maches.matches()) {
                    patron = Pattern.compile(VarImprimir);
                    maches = patron.matcher(g);
                    if (!maches.matches()) {
                        patron = Pattern.compile(sicloFor);
                        maches = patron.matcher(g);
                        if (!maches.matches()) {
                            patron = Pattern.compile(sicloWhile);
                            maches = patron.matcher(g);
                            if (!maches.matches()) {
                                patron = Pattern.compile(DeclaracionIF);
                                maches = patron.matcher(g);
                                if (!maches.matches()) {
                                    patron = Pattern.compile(ProgFin);
                                    maches = patron.matcher(g);
                                    if (!maches.matches()) {
                                        if (BuscarID(g) == 1) {System.out.print(",    invalido ");}
                                    } else {System.out.print("  ,   Fin del programa");}
                                } else {System.out.print("  ,   Condicional");}
                            } else {System.out.print("  ,   Condicional");}
                        } else {System.out.print("  ,   ciclo contado");}
                    } else {System.out.print("  ,   impreción");}
                } else {System.out.print("  ,   Lectura");}
            } else {System.out.print("  ,   Declaración de variables");}
        } else {System.out.print("  ,   Operación aritmetica");}
        System.out.print("\n");
    }
    public static String numerico = "[0-9]+([\\.][0-9]+)?";
    public static String Declarar = "(INICIO|INT|FLOAT|CADE|BOOLEAN)";
    public static String simbolo = "([\\|]|[\\.]|[\\,]|[\\;])";
    public static String agrupacion = "([\\(]|[\\)]|[\\{]|\".*\")";
    public static  String aritmetico = "((\\+)|(-)|(\\*)|(/)|(<)|(>)|(=)|(&)|(\\++)|(--)|(<=)|(>=)|(<>))";
    public static String token2 = "";
    public static String id;
    public static void AnalizarToken() {
        for (int i = 0; i < GuardaLinea.size(); i++) {
            String g = GuardaLinea.get(i);
            patron = Pattern.compile(numerico);
            maches = patron.matcher(g);
            if (!maches.matches()) {
                patron = Pattern.compile(Declarar);
                maches = patron.matcher(g);
                if (!maches.matches()) {
                    patron = Pattern.compile(reservado);
                    maches = patron.matcher(g);
                    if (!maches.matches()) {
                        patron = Pattern.compile(simbolo);
                        maches = patron.matcher(g);
                        if (!maches.matches()) {
                            patron = Pattern.compile(agrupacion);
                            maches = patron.matcher(g);
                            if (!maches.matches()) {
                                patron = Pattern.compile(aritmetico);
                                maches = patron.matcher(g);
                                if (!maches.matches()) {
                                    patron = Pattern.compile(Declarar);
                                    maches = patron.matcher(token2);
                                    if (maches.matches()) {
                                        patron = Pattern.compile(Declarar);
                                        maches = patron.matcher(token2);
                                        if (!maches.matches()) {
                                            System.out.println(",   invalido");
                                            Errores.add("003");
                                            combierte=combierte+contFil2;
                                            ErrorFila.add(combierte);
                                            combierte="";
                                        }else{System.out.print(g+",   identificador");id=g;if (BuscarID(g) == 0) {llenarTabla(g, token2);} }
                                    }else {erroresID(g);}
                                } else {
                                    System.out.print(g+",   aritmetico");
                                    if (g.equals("=")) {
                                        erroresOperaciones();
                                    }
                                }
                            }else {System.out.print(g+",   agrupacion");darValores(g);}
                        } else {System.out.print(g+",   simbolo");}
                    } else {
                        patron = Pattern.compile(Declarar);
                        maches = patron.matcher(token2);
                        if (maches.matches()) {
                            System.out.println(g+",   Palabra reservada");erroresID(g);
                        }
                        else{
                            System.out.print(g+",   reservado");
                            darValores(g);
                            erroresLectura();
                            erroresImprecion();
                            if (("IF".equals(g)||"WHILE".equals(g))&&i==0) {
                                erroresif();
                            }
                            if ("FOR".equals(g)&&i==0) {
                                erroresContados();
                            }
                        }
                    }
                } else {System.out.print(g+",    declaración");}
            } else { System.out.print(g+",    numerico"); darValores(g);
            }
        token2 = g;
        System.out.print("\n");
        }
        System.out.print("\n");
    }
    public static void darValores(String g){
        if (("INT".equals(GuardaLinea.get(0))||"FLOAT".equals(GuardaLinea.get(0)))&&"=".equals(GuardaLinea.get(2))) {
            if (BuscarTipo(id, GuardaLinea.get(0))==1) {
                DarValor(id,g);
                id="";
            }
        }
        if (("TRUE".equals(g)||"FALSE".equals(g))&&"BOOLEAN".equals(GuardaLinea.get(0))&&"=".equals(GuardaLinea.get(2))) {
            if (BuscarTipo(id, GuardaLinea.get(0))==1) {
                DarValor(id,g);
                id="";
            }
        }
        if ("CADE".equals(GuardaLinea.get(0)) && g.startsWith("\"") && "=".equals(GuardaLinea.get(2))) {
            if (BuscarTipo(id, GuardaLinea.get(0))==1) {
                DarValor(id,g);
                id="";
            }
        }
    }
    public static void erroresID(String g){
        if (BuscarID(g)!= 1) {
            if (BuscarID(g)== 2) {
                Errores.add("011");
                combierte=combierte+contFil2;
                ErrorFila.add(combierte);
                combierte="";
            }
            else{
                System.out.print(g+",   identificador");
                Errores.add("010");
                combierte=combierte+contFil2;
                ErrorFila.add(combierte);
                combierte="";
            }
        }else{System.out.print(g+",   identificador");}
    }
    public static void erroresLectura(){
        if ("LEER".equals(GuardaLinea.get(0))) {
            patron = Pattern.compile(ID);
            maches = patron.matcher(GuardaLinea.get(2));
            if (!maches.matches()) {
                Errores.add("013");
                combierte=combierte+contFil2;
                ErrorFila.add(combierte);
                combierte="";
            }
        }
    }
    public static void erroresImprecion(){
        if ("IMPRIMIR".equals(GuardaLinea.get(0))) {
            patron = Pattern.compile(entero);
            maches = patron.matcher(GuardaLinea.get(1));
            if (!maches.matches()) {
                patron = Pattern.compile(Flotante);
                maches = patron.matcher(GuardaLinea.get(1));
                if (!maches.matches()) {
                    patron = Pattern.compile(cadena);
                    maches = patron.matcher(GuardaLinea.get(1));
                    if (!maches.matches()) {
                        patron = Pattern.compile(ID);
                        maches = patron.matcher(GuardaLinea.get(1));
                        if (maches.matches()) {
                            if (BuscarID(GuardaLinea.get(1))==2){
                                Errores.add("015");
                                combierte=combierte+contFil2;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                        }
                        else{
                            Errores.add("014");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                }
            }
        }
    }
    public static void erroresif(){
        patron = Pattern.compile(entero);
        maches = patron.matcher(GuardaLinea.get(2));
        if (maches.matches()) {
            patron = Pattern.compile(entero);
            maches = patron.matcher(GuardaLinea.get(4));
            if (!maches.matches()) {
                patron = Pattern.compile(ID);
                maches = patron.matcher(GuardaLinea.get(4));
                if (!maches.matches()) {}
                else{
                    if (BuscarID(GuardaLinea.get(4))==2) {
                        //No se pueden usar palabras reservada
                        Errores.add("012");
                        combierte=combierte+contFil2;
                        ErrorFila.add(combierte);
                        combierte="";
                    }
                    else{
                        if (BuscarTipo(GuardaLinea.get(4), "INT")!=1) {
                            Errores.add("017");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        } 
                    }
                }
            }
        }
        else{
            patron = Pattern.compile(boleano);
            maches = patron.matcher(GuardaLinea.get(2));
            if (maches.matches()) {
                patron = Pattern.compile(boleano);
                maches = patron.matcher(GuardaLinea.get(4));
                if (!maches.matches()) {
                    patron = Pattern.compile(ID);
                    maches = patron.matcher(GuardaLinea.get(4));
                    if (!maches.matches()) {}
                    else{
                        if (BuscarID(GuardaLinea.get(4))==2&&!"TRUE".equals(GuardaLinea.get(4))&&!"FALSE".equals(GuardaLinea.get(4))) {
                            //No se pueden usar palabras reservada
                            Errores.add("012");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                        else{
                            if ((BuscarTipo(GuardaLinea.get(4), "BOOLEAN")!=1)&&!"TRUE".equals(GuardaLinea.get(4))&&!"FALSE".equals(GuardaLinea.get(4))) {
                                Errores.add("016");
                                combierte=combierte+contFil2;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                        }
                    }
                }
                else{
                    Errores.add("016");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }
            else{
                patron = Pattern.compile(Flotante);
                maches = patron.matcher(GuardaLinea.get(2));
                if (maches.matches()) {
                    patron = Pattern.compile(Flotante);
                    maches = patron.matcher(GuardaLinea.get(4));
                    if (!maches.matches()) {
                        patron = Pattern.compile(ID);
                        maches = patron.matcher(GuardaLinea.get(4));
                        if (!maches.matches()) {}
                        else{
                            if (BuscarID(GuardaLinea.get(4))==2) {
                                //No se pueden usar palabras reservada
                                Errores.add("012");
                                combierte=combierte+contFil2;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                            else{
                                if (BuscarTipo(GuardaLinea.get(4), "FLOAT")!=1) {
                                    Errores.add("016");
                                    combierte=combierte+contFil2;
                                    ErrorFila.add(combierte);
                                    combierte="";
                                } 
                            }
                        }
                    }
                }else{
                    patron = Pattern.compile(ID);
                    maches = patron.matcher(GuardaLinea.get(2));
                    if (maches.matches()) {
                        if (BuscarID(GuardaLinea.get(2))==1) {
                            patron = Pattern.compile(ID);
                            maches = patron.matcher(GuardaLinea.get(4));
                            if (!maches.matches()) {
                                patron = Pattern.compile(entero);
                                maches = patron.matcher(GuardaLinea.get(4)); 
                                if (maches.matches()) {
                                    if (ExtraerTipo(GuardaLinea.get(2)).equals("INT")) {}
                                    else{
                                        Errores.add("016");
                                        combierte=combierte+contFil2;
                                        ErrorFila.add(combierte);
                                        combierte="";
                                    }
                                }
                                else{
                                    patron = Pattern.compile(boleano);
                                    maches = patron.matcher(GuardaLinea.get(4));
                                    if (maches.matches()) {
                                        if (ExtraerTipo(GuardaLinea.get(2)).equals("BOOLEAN")) {}
                                        else{
                                            Errores.add("016");
                                            combierte=combierte+contFil2;
                                            ErrorFila.add(combierte);
                                            combierte="";
                                        }
                                    }
                                    else{
                                        patron = Pattern.compile(cadena);
                                        maches = patron.matcher(GuardaLinea.get(4));
                                        if (maches.matches()) {
                                            if (ExtraerTipo(GuardaLinea.get(2)).equals("CADE")) {}
                                            else{
                                                Errores.add("016");
                                                combierte=combierte+contFil2;
                                                ErrorFila.add(combierte);
                                                combierte="";
                                            }
                                        }
                                        else{
                                            patron = Pattern.compile(Flotante);
                                            maches = patron.matcher(GuardaLinea.get(4));
                                            if (maches.matches()) {
                                                if (ExtraerTipo(GuardaLinea.get(2)).equals("FLOAT")) {}
                                                else{
                                                    Errores.add("016");
                                                    combierte=combierte+contFil2;
                                                    ErrorFila.add(combierte);
                                                    combierte="";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                if (!ExtraerTipo(GuardaLinea.get(2)).equals(ExtraerTipo(GuardaLinea.get(4)))) {
                                    Errores.add("016");
                                    combierte=combierte+contFil2;
                                    ErrorFila.add(combierte);
                                    combierte="";
                                }
                            }
                        }
                        else{
                            if (BuscarID(GuardaLinea.get(2))==2) {
                                Errores.add("012");
                                combierte=combierte+contFil2;
                                ErrorFila.add(combierte);
                                combierte="";
                            }
                        }
                    }
                }
            }
        }
        if ("IF".equals(GuardaLinea.get(0))) {
            patron = Pattern.compile(cadena);
            maches = patron.matcher(GuardaLinea.get(2));
            if (maches.matches()) {
                patron = Pattern.compile(cadena);
                maches = patron.matcher(GuardaLinea.get(4));
                if (maches.matches()) {
                    Errores.add("017");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }
        }
        patron = Pattern.compile(ID);
        maches = patron.matcher(GuardaLinea.get(6));
        if (maches.matches()) {
            if (BuscarID(GuardaLinea.get(6))==1) {
                if ("INT".equals(ExtraerTipo(GuardaLinea.get(6)))||"FLOAT".equals(ExtraerTipo(GuardaLinea.get(6)))||"CADE".equals(ExtraerTipo(GuardaLinea.get(6)))) {
                    if (ExtraerTipo(GuardaLinea.get(4)).equals(ExtraerTipo(GuardaLinea.get(6)))||ExtraerTipo(GuardaLinea.get(6)).equals(validarTipo(GuardaLinea.get(4))));
                    else{
                        Errores.add("016");
                        combierte=combierte+contFil2;
                        ErrorFila.add(combierte);
                        combierte="";
                    }
                }else{
                    Errores.add("016");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }else{
                if (BuscarID(GuardaLinea.get(6))==2) {
                    Errores.add("016");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }
        }
        else{
            patron = Pattern.compile(entero);
            maches = patron.matcher(GuardaLinea.get(6));
            if (maches.matches()) {
                if (ExtraerTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6)))||validarTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6))));
                else{
                    Errores.add("016");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }
            else{
                patron = Pattern.compile(Flotante);
                maches = patron.matcher(GuardaLinea.get(6));
                if (maches.matches()) {
                    if (ExtraerTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6)))||validarTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6))));
                    else{
                        Errores.add("016");
                        combierte=combierte+contFil2;
                        ErrorFila.add(combierte);
                        combierte="";
                    }
                }
                else{
                    patron = Pattern.compile(cadena);
                    maches = patron.matcher(GuardaLinea.get(6));
                    if (maches.matches()) {
                        if (ExtraerTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6)))||validarTipo(GuardaLinea.get(4)).equals(validarTipo(GuardaLinea.get(6))));
                        else{
                            Errores.add("016");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                }
            }
        }
    }
    public static void erroresContados(){
        patron = Pattern.compile(ID);
        maches = patron.matcher(GuardaLinea.get(2));
        if (maches.matches()) {
            if (BuscarID(GuardaLinea.get(2))==1) {
                if (ExtraerTipo(GuardaLinea.get(2)).equals("INT")) {
                }else{
                    Errores.add("016");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }else{
                if (BuscarID(GuardaLinea.get(2))==2) {
                    Errores.add("012");
                    combierte=combierte+contFil2;
                    ErrorFila.add(combierte);
                    combierte="";
                }
            }
        }
        if (!ExtraerTipo(GuardaLinea.get(2)).equals("")) {
            for (int i = 4; i < GuardaLinea.size(); i++) {
                patron = Pattern.compile(ID);
                maches = patron.matcher(GuardaLinea.get(i));
                if (maches.matches()) {
                    if (BuscarID(GuardaLinea.get(i))==1) {
                        if (!ExtraerTipo(GuardaLinea.get(2)).equals(ExtraerTipo(GuardaLinea.get(i)))) {
                            Errores.add("016");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                    else{
                        if (BuscarID(GuardaLinea.get(i))==2) {
                            Errores.add("012");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                }
            }
        }
    }
    public static void erroresOperaciones(){
        if ((GuardaLinea.size()==4||GuardaLinea.size()==6)&&"=".equals(GuardaLinea.get(1))) {
            if (!ExtraerTipo(GuardaLinea.get(0)).equals(ExtraerTipo(GuardaLinea.get(2)))&&!ExtraerTipo(GuardaLinea.get(0)).equals(validarTipo(GuardaLinea.get(2)))) {
                Errores.add("016");
                combierte=combierte+contFil2;
                ErrorFila.add(combierte);
                combierte="";
            }
            else{
                patron = Pattern.compile(operadorArit);
                maches = patron.matcher(GuardaLinea.get(3));
                if (maches.matches()) {
                    patron = Pattern.compile(ID);
                    maches = patron.matcher(GuardaLinea.get(2));
                    if (maches.matches()) {
                        if (!ExtraerTipo(GuardaLinea.get(2)).equals(ExtraerTipo(GuardaLinea.get(4)))&&!ExtraerTipo(GuardaLinea.get(2)).equals(validarTipo(GuardaLinea.get(4)))) {
                            Errores.add("016");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                    else{
                        if (!validarTipo(GuardaLinea.get(2)).equals(ExtraerTipo(GuardaLinea.get(4)))&&!validarTipo(GuardaLinea.get(2)).equals(validarTipo(GuardaLinea.get(4)))) {
                            Errores.add("016");
                            combierte=combierte+contFil2;
                            ErrorFila.add(combierte);
                            combierte="";
                        }
                    }
                }
            }
        }
    }
    public static  int BuscarID(String g) {
        int c = 0;
        if (!IDTabla.isEmpty()) {
            for (int i = 0; i < IDTabla.size(); i++) {
                if (!g.equals(IDTabla.get(i))) {
                    patron = Pattern.compile(reservado);
                    maches = patron.matcher(g);
                    if (maches.matches()) {c=2;}
                }else{c=1;}
            }
        }return c;
    }
    public static int BuscarTipo(String g, String Tip){
        int c=0;
        if (!IDTabla.isEmpty()) {
            for (int i = 0; i < IDTabla.size(); i++) {
                if (g.equals(IDTabla.get(i))&& Tip.equals(TipoTabla.get(i))) {
                    c=1;
                }
            }
        }
        return c;
    }
    public static  void llenarTabla(String IDentificador, String TIPO) {
        IDTabla.add(IDentificador);
        TipoTabla.add(TIPO);
        if ("INT".equals(TIPO)){ValorTabla.add("0");}
        if ("CADE".equals(TIPO)){ValorTabla.add("");}
        if ("FLOAT".equals(TIPO)){ValorTabla.add("0.0");}
        if ("INICIO".equals(TIPO)){ValorTabla.add("");}
        if ("BOOLEAN".equals(TIPO)){ValorTabla.add("FALSE");}
    }
    public static void DarValor(String g, String Val){
        for (int i = 0; i < IDTabla.size(); i++) {
            if (g.equals(IDTabla.get(i))) {
                ValorTabla.set(i, Val);
            }
        }
    } 
    public static String ExtraerTipo(String g){
        String c="";
        if (!IDTabla.isEmpty()) {
            for (int i = 0; i < IDTabla.size(); i++) {
                if (g.equals(IDTabla.get(i))) {
                    c=c+TipoTabla.get(i);
                }
            }
        }
        return c;
    }
    public static String validarTipo(String g){
        String c="";
        patron = Pattern.compile(entero);
        maches = patron.matcher(g);
        if (maches.matches()) {
            c="INT";
        }
        patron = Pattern.compile(cadena);
        maches = patron.matcher(g);
        if (maches.matches()) {
            c="CADE";
        }
        patron = Pattern.compile(Flotante);
        maches = patron.matcher(g);
        if (maches.matches()) {
            c="FLOAT";
        }
        patron = Pattern.compile(boleano);
        maches = patron.matcher(g);
        if (maches.matches()) {
            c="BOOLEAN";
        }
        return c;
    }
}