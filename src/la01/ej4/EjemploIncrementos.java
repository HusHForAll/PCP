package la01.ej4;

// ============================================================================
class CuentaIncrementos {
// ============================================================================
  long contador = 0;

  // --------------------------------------------------------------------------
  void incrementaContador() {
    contador++;
  }

  // --------------------------------------------------------------------------
  long dameContador() {
    return( contador );
  }
}


// ============================================================================
class MiHebra extends Thread {
// ============================================================================
  // Declaracion de variables
  int miId;
  CuentaIncrementos c;


  // --------------------------------------------------------------------------
  // Definicion del constructor, si es necesario
  public MiHebra( int miId, CuentaIncrementos c ) {
    this.miId = miId;
    this.c = c;
  }

  // --------------------------------------------------------------------------
  public void run() {
    System.out.println( "Hebra: " + miId + " Comenzando incrementos" );
    // Bucle de 1000000 incrementos del objeto compartido
    for( int i = 0; i < 1000000; i++ ) {
      c.incrementaContador();
    }
    System.out.println( "Hebra: " + miId + " Terminando incrementos" );
  }
}

// ============================================================================
class EjemploIncrementos {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int  numHebras;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 1 ) {
      System.err.println( "Uso: java programa <numHebras>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
      if( numHebras <= 0 ) {
        System.err.println( "Uso: [ java programa <numHebras> ] donde numHebras > 0" );
        System.exit( -1 );
      }
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }
    System.out.println( "numHebras: " + numHebras );

    // --------  INCLUIR NUEVO CODIGO A CONTINUACION --------------------------
    // 2. Crear e inicializar el objeto compartido
    CuentaIncrementos conta = new CuentaIncrementos();

    // 3. Imprimir el valor inicial del contador
    System.out.println( "Valor inicial del contador: " + conta.dameContador() );

    // 4. Crear y arrancar las hebras, utilizando un vector de hebras
    MiHebra[] v = new MiHebra[ numHebras ];
    for( int i = 0; i < numHebras; i++ ) {
      v[ i ] = new MiHebra( i, conta );
      v[ i ].start();
    }

    // 5. Esperar a que todas las hebras finalicen
    try {
      for( int i = 0; i < numHebras; i++ ) {
        v[ i ].join();
      }
    } catch( InterruptedException ex ) {
      System.out.println( "Interrupcion en la espera" );
    }

    // 6. Imprimir el valor final del contador
    System.out.println( "Valor final del contador: " + conta.dameContador() );
  }
}

