package fr.istic.info.taa.classcounter_maven_plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.clapper.classutil.ClassFinder;
import org.clapper.classutil.ClassInfo;

/**
 * A goal to generate code.
 *
 * @goal count
 * @phase compile
 */
public class ClassCounterMojo extends AbstractMojo{

	/**
	 * Location of the file.
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Message language
	 * @parameter default-value="french"
	 * @required
	 */
	private String language;

	@SuppressWarnings("resource")
	public void execute() throws MojoExecutionException, MojoFailureException {


		//Creation d'un fichier 
		File f = outputDirectory;

		if ( !f.exists() )
		{
			f.mkdirs();
		}

		File touch = new File( f, "counter.txt" );

		FileWriter w = null;
		try
		{
			w = new FileWriter( touch );
		}
		catch ( IOException e )
		{
			throw new MojoExecutionException( "Error creating file " + touch, e );
		}

		//On recopie dans un fichier, tout ce qui est en Log sur la console

		//Iindication de debut du MOJO
		getLog().info("Ce plugin genère un fichier \"count.txt\" dans le repertoire \"target\" du projet qui l\'utilise");
		getLog().info("Indication will be given in the following language \"" + language + "\"\n");
		try {
			w.write("Indication will be given in the following language \"" + language + "\"\n");
		} catch (IOException e) {
			throw new MojoExecutionException( "Error writing in file " + w, e );
		}

		//Le repertoire du projet
		File f1 = new File(outputDirectory.getAbsolutePath() + File.separator+ "classes");

		//Tous les fichiers dans une liste
		List<File> files = new ArrayList<File>();
		files.add(f1);

		//ClassFinder de scala pour repertorier les fichiers
		ClassFinder finder = new ClassFinder(scala.collection.JavaConversions.asScalaBuffer(files));

		int nbrClasses=finder.getClasses().size();
		String francais = "nombre de classe " +  nbrClasses + "\n";
		String english = "number of classes " + nbrClasses + "\n";
		//Selon la langue du fichier
		if ("french".equals(language)){
			this.getLog().info(francais);
			try {
				w.write(francais);
			} catch (IOException e) {
				throw new MojoExecutionException( "Error writing in file " + w, e );
			}
		}
		else{
			this.getLog().info(english);
			try {
				w.write(english);
			} catch (IOException e) {
				throw new MojoExecutionException( "Error writing in file " + w, e );
			}
		}

		//On detail les atrtributs
		scala.collection.Iterator<ClassInfo> it = finder.getClasses();
		while (it.hasNext()){
			ClassInfo c = it.next();
			if ("french".equals(language)) {
				this.getLog().info("\t Pour la classe " + c.name());
				this.getLog().info("\t \t Nbre attributs " + c.fields().size());
				this.getLog().info("\t \t Nbre methodes " + c.methods().size());
				try {
					w.write("\t Pour la classe " + c.name() + "\n");
					w.write("\t \t Nbre attributs " + c.fields().size() + "\n");
					w.write("\t \t Nbre methodes " + c.methods().size() + "\n");
				} catch (IOException e) {
					throw new MojoExecutionException( "Error writing in file " + w, e );
				}				
			}else{
				this.getLog().info("\t For the class named " + c.name());
				this.getLog().info("\t \t Number of filed " + c.fields().size());
				this.getLog().info("\t \t Number of methods " + c.methods().size());
				try {
					w.write("\t For the class named " + c.name() + "\n");
					w.write("\t \t Number of filed " + c.fields().size() + "\n");
					w.write("\t \t Number of methods " + c.methods().size());
				} catch (IOException e) {
					throw new MojoExecutionException( "Error writing in file " + w, e );
				}
			}
		}

		//fermeture du fichier
		if ( w != null )
		{
			try
			{
				w.close();
			}
			catch ( IOException e )
			{
				// ignore
			}
		}
	}

}