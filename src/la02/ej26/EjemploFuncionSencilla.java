package la02.ej26;

// ============================================================================
class EjemploFuncionSencilla {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int     n, numHebras;
    long    t1, t2;
    double  sumaX, sumaY, ts, tc, tb;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.err.println( "Uso: java programa <numHebras> <n>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
      n         = Integer.parseInt( args[ 1 ] );
      if( ( numHebras <= 0 ) || ( n <= 0 ) ) {
        System.err.print( "Uso: [ java programa <numHebras> <n> ] " );
        System.err.println( "donde ( numHebras > 0 )  y ( n > 0 )" );
        System.exit( -1 );
      }
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      n         = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }

    // Crea los vectores.
    double vectorX[] = new double[ n ];
    double vectorY[] = new double[ n ];

    //
    // Implementacion secuencial (sin temporizar).
    //
    inicializaVectorX( vectorX );
    inicializaVectorY( vectorY );
    for( int i = 0; i < n; i++ ) {
      vectorY[ i ] = evaluaFuncion( vectorX[ i ] );
    }

    //
    // Implementacion secuencial.
    //
    inicializaVectorX( vectorX );
    inicializaVectorY( vectorY );
    t1 = System.nanoTime();
    for( int i = 0; i < n; i++ ) {
      vectorY[ i ] = evaluaFuncion( vectorX[ i ] );
    }
    t2 = System.nanoTime();
    ts = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Tiempo secuencial (seg.):                    " + ts );
    //// imprimeResultado( vectorX, vectorY );
    // Comprueba el resultado. 
    sumaX = sumaVector( vectorX );
    sumaY = sumaVector( vectorY );
    System.out.println( "Suma del vector X:          " + sumaX );
    System.out.println( "Suma del vector Y:          " + sumaY );

    //
    // Implementacion paralela ciclica.
    //
    inicializaVectorX( vectorX );
    inicializaVectorY( vectorY );
    t1 = System.nanoTime();
    // Gestion de hebras para la implementacion paralela ciclica
    MiHebraCiclica vc[] = new MiHebraCiclica[numHebras];
    for (int i = 0; i < numHebras; i++) {
        vc[i] = new MiHebraCiclica(i, n, numHebras, vectorX, vectorY);
        vc[i].start();
    }
    for (int i = 0; i < numHebras; i++) {
        try {
            vc[i].join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    t2 = System.nanoTime();
    tc = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Tiempo paralela ciclica (seg.):              " + tc );
    System.out.println( "Incremento paralela ciclica:                 " + (ts / tc) ); // (B)
    //// imprimeResultado( vectorX, vectorY );
    // Comprueba el resultado. 
    sumaX = sumaVector( vectorX );
    sumaY = sumaVector( vectorY );
    System.out.println( "Suma del vector X:          " + sumaX );
    System.out.println( "Suma del vector Y:          " + sumaY );

    //
    // Implementacion paralela por bloques.
    //
    inicializaVectorX( vectorX );
    inicializaVectorY( vectorY );
    t1 = System.nanoTime();
    // Gestion de hebras para la implementacion paralela por bloques
    MiHebraBloque vb[] = new MiHebraBloque[numHebras];
    for (int i = 0; i < numHebras; i++) {
        vb[i] = new MiHebraBloque(i, n, numHebras, vectorX, vectorY);
        vb[i].start();
    }
    for (int i = 0; i < numHebras; i++) {
        try {
            vb[i].join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    t2 = System.nanoTime();
    tc = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Tiempo paralela por bloques (seg.):              " + tc );
    System.out.println( "Incremento paralela por bloques:                 " + (ts / tc) ); // (B)
    //// imprimeResultado( vectorX, vectorY );
    // Comprueba el resultado.
    sumaX = sumaVector( vectorX );
    sumaY = sumaVector( vectorY );
    System.out.println( "Suma del vector X:          " + sumaX );
    System.out.println( "Suma del vector Y:          " + sumaY );

    System.out.println( "Fin del programa." );
  }

  // --------------------------------------------------------------------------
  static void inicializaVectorX( double vectorX[] ) {
    if( vectorX.length == 1 ) {
      vectorX[ 0 ] = 0.0;
    } else {
      for( int i = 0; i < vectorX.length; i++ ) {
        vectorX[ i ] = 10.0 * ( double ) i / ( ( double ) vectorX.length - 1 );
      }
    }
  }

  // --------------------------------------------------------------------------
  static void inicializaVectorY( double vectorY[] ) {
    for( int i = 0; i < vectorY.length; i++ ) {
      vectorY[ i ] = 0.0;
    }
  }

  // --------------------------------------------------------------------------
  static double sumaVector( double vector[] ) {
    double  suma = 0.0;
    for( int i = 0; i < vector.length; i++ ) {
      suma += vector[ i ];
    }
    return suma;
  }

  // --------------------------------------------------------------------------
  static double evaluaFuncion( double x ) {
    return 5.4 * x;
  }

  // --------------------------------------------------------------------------
  static void imprimeVector( double vector[] ) {
    for( int i = 0; i < vector.length; i++ ) {
      System.out.println( " vector[ " + i + " ] = " + vector[ i ] );
    }
  }

  // --------------------------------------------------------------------------
  static void imprimeResultado( double vectorX[], double vectorY[] ) {
    for( int i = 0; i < Math.min( vectorX.length, vectorY.length ); i++ ) {
      System.out.println( "  i: " + i + 
                          "  x: " + vectorX[ i ] +
                          "  y: " + vectorY[ i ] );
    }
  }

}

// Crea las clases adicionales que sean necesarias
class MiHebraCiclica extends Thread{
  int miId, n, numHebras;
  double[] vectorX, vectorY;

  public MiHebraCiclica( int miId, int n, int numHebras, double[] vectorX, double[] vectorY ) {
    this.miId = miId;
    this.n = n;
    this.numHebras = numHebras;
    this.vectorX = vectorX;
    this.vectorY = vectorY;
  }
  public void run() {
    for ( int i = miId; i < n; i += numHebras ) {
      vectorY[ i ] = EjemploFuncionSencilla.evaluaFuncion( vectorX[ i ] );
    }
  }
}

class MiHebraBloque extends Thread{
  int miId, n, numHebras;
  double[] vectorX, vectorY;

  public MiHebraBloque( int miId, int n, int numHebras, double[] vectorX, double[] vectorY ) {
    this.miId = miId;
    this.n = n;
    this.numHebras = numHebras;
    this.vectorX = vectorX;
    this.vectorY = vectorY;
  }
  public void run() {
    int tam = (n + numHebras - 1) / numHebras;
    int ini = tam * miId;
    int fin = Math.min(n, ini + tam);
    for ( int i = ini; i < fin; i++ ) {
      vectorY[ i ] = EjemploFuncionSencilla.evaluaFuncion( vectorX[ i ] );
    }
  }
}