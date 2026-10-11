package la02.ej1;

// ============================================================================
class EjemploMuestraNumeros {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int  n, numHebras;

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
    //
    // Implementacion paralela con distribucion ciclica o por bloques.
    //
    /*
    // Crea un vector de hebras. Crea y arranca las hebras
    MiHebraCiclica v[] = new MiHebraCiclica[numHebras];
    for (int i = 0; i < numHebras; i++) {
      v[i] = new MiHebraCiclica(i, numHebras, n);
      v[i].start();
    }

    // Espera a que terminen todas las hebras.
    for (int i = 0; i < numHebras; i++) {
      try {
        v[i].join();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    */
    // Crea un vector de hebras. Crea y arranca las hebras.
    MiHebraBloques v[] = new MiHebraBloques[numHebras];
    for (int i = 0; i < numHebras; i++) {
      v[i] = new MiHebraBloques(i, numHebras, n);
      v[i].start();
    }

    // Espera a que terminen todas las hebras.
    for (int i = 0; i < numHebras; i++) {
      try {
        v[i].join();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }

  }
}

// Crea las clases adicionales que sean necesarias
class MiHebraCiclica extends Thread {
  int miId, numHebras, n;

  MiHebraCiclica(int miId, int numHebras, int n) {
    this.miId = miId;
    this.numHebras = numHebras;
    this.n = n;
  }

  public void run() {
    for (int i = miId; i < n; i += numHebras) {
      System.out.println("Hebra " + miId + ": " + i);
    }
  }
}

class MiHebraBloques extends Thread {
  int miId, numHebras, n;

  MiHebraBloques(int miId, int numHebras, int n) {
    this.miId = miId;
    this.numHebras = numHebras;
    this.n = n;
  }

  public void run() {
    int tamBloque = (n + numHebras - 1) / numHebras;
    int iniElem = miId * tamBloque;
    int finElem = Math.min(n, iniElem + tamBloque);
    for (int i = iniElem; i < finElem; i++) {
      System.out.println("Hebra " + miId + ": " + i);
    }
  }
}
