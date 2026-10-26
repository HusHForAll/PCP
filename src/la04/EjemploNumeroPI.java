package la04;

import java.util.concurrent.atomic.DoubleAdder;

// ===========================================================================
class EjemploNumeroPI {
// ===========================================================================

  // -------------------------------------------------------------------------
  public static void main( String args[] ) {
    long                        numRectangulos;
    double                      baseRectangulo, x, suma, pi;
    int                         numHebras;
    long                        t1, t2;
    double                      tSec, tPar;
    Acumula                     a;
    MiHebraMultAcumulaciones  vt[];
    MiHebraUnaAcumulacion     vt2[];
    MiHebraMultAcumulacionAtomica vt3[];
    MiHebraUnaAcumulacionAtomica  vt4[];
    DoubleAdder                 sumaAt;

    // Comprobacion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.out.println( "ERROR: numero de argumentos incorrecto.");
      System.out.println( "Uso: java programa <numHebras> <numRectangulos>" );
      System.exit( -1 );
    }
    try {
      numHebras      = Integer.parseInt( args[ 0 ] );
      numRectangulos = Long.parseLong( args[ 1 ] );
      if( ( numHebras <= 0 ) || ( numRectangulos <= 0 ) ) {
        System.err.print( "Uso: [ java programa <numHebras> <n> ] " );
        System.err.println( "donde ( numHebras > 0 ) y ( numRectangulos > 0 )" );
        System.exit( -1 );
      }
    } catch( NumberFormatException ex ) {
      numHebras      = -1;
      numRectangulos = -1;
      System.out.println( "ERROR: Numeros de entrada incorrectos." );
      System.exit( -1 );
    }

    System.out.println();
    System.out.println( "Calculo del numero PI mediante integracion." );

    //
    // Calculo del numero PI de forma secuencial.
    //
    System.out.println();
    System.out.println( "Inicio del calculo secuencial." );
    t1 = System.nanoTime();
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    suma           = 0.0;
    for( long i = 0; i < numRectangulos; i++ ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      suma += f( x );
    }
    pi = baseRectangulo * suma;
    t2 = System.nanoTime();
    tSec = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Version secuencial. Numero PI: " + pi );
    System.out.println( "Tiempo secuencial (s.):        " + tSec );
    //
    // Calculo del numero PI de forma paralela: 
    // Multiples acumulaciones por hebra.
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Multiples acumulaciones por hebra." );
    t1 = System.nanoTime();
    // (A)
    a  = new Acumula();
    vt = new MiHebraMultAcumulaciones[ numHebras ];
    for( int i = 0; i < numHebras; i++ ) {
      vt[ i ] = new MiHebraMultAcumulaciones( i, numHebras, numRectangulos, a );
      vt[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        vt[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    pi = baseRectangulo * a.dameDato();
    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec / tPar );

    //
    // Calculo del numero PI de forma paralela: 
    // Una acumulacion por hebra.
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Una acumulacion por hebra." );
    t1 = System.nanoTime();
    // (B)
    a   = new Acumula();
    vt2 = new MiHebraUnaAcumulacion[ numHebras ];
    for( int i = 0; i < numHebras; i++ ) {
      vt2[ i ] = new MiHebraUnaAcumulacion( i, numHebras, numRectangulos, a );
      vt2[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        vt2[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    pi = baseRectangulo * a.dameDato();
    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec / tPar );

    //
    // Calculo del numero PI de forma paralela: 
    // Multiples acumulaciones por hebra (Atomica)
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Multiples acumulaciones por hebra (At)." );
    t1 = System.nanoTime();
    // (C)
    sumaAt = new DoubleAdder();
    vt3    = new MiHebraMultAcumulacionAtomica[ numHebras ];
    for( int i = 0; i < numHebras; i++ ) {
      vt3[ i ] = new MiHebraMultAcumulacionAtomica( i, numHebras, numRectangulos, sumaAt );
      vt3[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        vt3[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    pi = baseRectangulo * sumaAt.sum();
    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec / tPar );

    //
    // Calculo del numero PI de forma paralela: 
    // Una acumulacion por hebra (Atomica).
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Una acumulacion por hebra (At)." );
    t1 = System.nanoTime();
    // (D)
    sumaAt = new DoubleAdder();
    vt4    = new MiHebraUnaAcumulacionAtomica[ numHebras ];
    for( int i = 0; i < numHebras; i++ ) {
      vt4[ i ] = new MiHebraUnaAcumulacionAtomica( i, numHebras, numRectangulos, sumaAt );
      vt4[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        vt4[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    pi = baseRectangulo * sumaAt.sum();
    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec / tPar );

    System.out.println();
    System.out.println( "Fin de programa." );
  }

  // -------------------------------------------------------------------------
  static double f( double x ) {
    return ( 4.0/( 1.0 + x*x ) );
  }
}


// ===========================================================================
class Acumula {
// ===========================================================================
  double  suma;

  // -------------------------------------------------------------------------
  Acumula() {
    // (E)
    suma = 0.0;
  }

  // -------------------------------------------------------------------------
  synchronized void acumulaDato( double dato ) {
    // (F)
    suma += dato;
  }

  // -------------------------------------------------------------------------
  synchronized double dameDato() {
    // (G)
    return suma;
  }
}

// ===========================================================================
class MiHebraMultAcumulaciones extends Thread {
// ===========================================================================
  int      miId, numHebras;
  long     numRectangulos;
  Acumula  a;

  // -------------------------------------------------------------------------
  MiHebraMultAcumulaciones( int miId, int numHebras, long numRectangulos,
                              Acumula a ) {
    // (H)
    this.miId           = miId;
    this.numHebras      = numHebras;
    this.numRectangulos = numRectangulos;
    this.a              = a;
  }

  // -------------------------------------------------------------------------
  public void run() {
    // (I)
    double baseRectangulo, x;
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    // Distribucion ciclica.
    for( long i = miId; i < numRectangulos; i += numHebras ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      a.acumulaDato( EjemploNumeroPI.f( x ) );
    }
  }
}

// ===========================================================================
class MiHebraUnaAcumulacion extends Thread {
// ===========================================================================
// (J)
  int      miId, numHebras;
  long     numRectangulos;
  Acumula  a;

  // -------------------------------------------------------------------------
  MiHebraUnaAcumulacion( int miId, int numHebras, long numRectangulos,
                           Acumula a ) {
    this.miId           = miId;
    this.numHebras      = numHebras;
    this.numRectangulos = numRectangulos;
    this.a              = a;
  }

  // -------------------------------------------------------------------------
  public void run() {
    double baseRectangulo, x, sumaL;
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    sumaL = 0.0;
    // Distribucion ciclica.
    for( long i = miId; i < numRectangulos; i += numHebras ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      sumaL += EjemploNumeroPI.f( x );
    }
    a.acumulaDato( sumaL );
  }
}

// ===========================================================================
class MiHebraMultAcumulacionAtomica extends Thread {
// ===========================================================================
// (K)
  int          miId, numHebras;
  long         numRectangulos;
  DoubleAdder  sumaAt;

  // -------------------------------------------------------------------------
  MiHebraMultAcumulacionAtomica( int miId, int numHebras, long numRectangulos,
                                   DoubleAdder sumaAt ) {
    this.miId           = miId;
    this.numHebras      = numHebras;
    this.numRectangulos = numRectangulos;
    this.sumaAt         = sumaAt;
  }

  // -------------------------------------------------------------------------
  public void run() {
    double baseRectangulo, x;
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    // Distribucion ciclica.
    for( long i = miId; i < numRectangulos; i += numHebras ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      sumaAt.add( EjemploNumeroPI.f( x ) );
    }
  }
}

// ===========================================================================
class MiHebraUnaAcumulacionAtomica extends Thread {
// ===========================================================================
// (L)
  int          miId, numHebras;
  long         numRectangulos;
  DoubleAdder  sumaAt;

  // -------------------------------------------------------------------------
  MiHebraUnaAcumulacionAtomica( int miId, int numHebras, long numRectangulos,
                                  DoubleAdder sumaAt ) {
    this.miId           = miId;
    this.numHebras      = numHebras;
    this.numRectangulos = numRectangulos;
    this.sumaAt         = sumaAt;
  }

  // -------------------------------------------------------------------------
  public void run() {
    double baseRectangulo, x, sumaL;
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    sumaL = 0.0;
    // Distribucion ciclica.
    for( long i = miId; i < numRectangulos; i += numHebras ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      sumaL += EjemploNumeroPI.f( x );
    }
    sumaAt.add( sumaL );
  }
}



