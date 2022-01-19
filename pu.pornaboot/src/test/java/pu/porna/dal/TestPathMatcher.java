package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * Over de glob: matcher:
 * 
 * <p>
 * When the syntax is "{@code glob}" then the {@code String} representation of the path is matched using a limited
 * pattern language that resembles regular expressions but with a simpler syntax. For example:
 *
 * <table class="striped" style="text-align:left; margin-left:2em">
 * <caption style="display:none">Pattern Language</caption> <thead>
 * <tr>
 * <th scope="col">Example
 * <th scope="col">Description
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <th scope="row">{@code *.java}</th>
 * <td>Matches a path that represents a file name ending in {@code .java}</td>
 * </tr>
 * <tr>
 * <th scope="row">{@code *.*}</th>
 * <td>Matches file names containing a dot</td>
 * </tr>
 * <tr>
 * <th scope="row">{@code *.{java,class}}</th>
 * <td>Matches file names ending with {@code .java} or {@code .class}</td>
 * </tr>
 * <tr>
 * <th scope="row">{@code foo.?}</th>
 * <td>Matches file names starting with {@code foo.} and a single character extension</td>
 * </tr>
 * <tr>
 * <th scope="row"><code>&#47;home&#47;*&#47;*</code>
 * <td>Matches <code>&#47;home&#47;gus&#47;data</code> on UNIX platforms</td>
 * </tr>
 * <tr>
 * <th scope="row"><code>&#47;home&#47;**</code>
 * <td>Matches <code>&#47;home&#47;gus</code> and <code>&#47;home&#47;gus&#47;data</code> on UNIX platforms</td>
 * </tr>
 * <tr>
 * <th scope="row"><code>C:&#92;&#92;*</code>
 * <td>Matches <code>C:&#92;foo</code> and <code>C:&#92;bar</code> on the Windows platform (note that the backslash is
 * escaped; as a string literal in the Java Language the pattern would be <code>"C:&#92;&#92;&#92;&#92;*"</code>)</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <p>
 * The following rules are used to interpret glob patterns:
 *
 * <ul>
 * <li>
 * <p>
 * The {@code *} character matches zero or more {@link Character characters} of a {@link Path#getName(int) name}
 * component without crossing directory boundaries.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The {@code **} characters matches zero or more {@link Character characters} crossing directory boundaries.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The {@code ?} character matches exactly one character of a name component.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The backslash character ({@code \}) is used to escape characters that would otherwise be interpreted as special
 * characters. The expression {@code \\} matches a single backslash and "\{" matches a left brace for example.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The {@code [ ]} characters are a <i>bracket expression</i> that match a single character of a name component out of a
 * set of characters. For example, {@code [abc]} matches {@code "a"}, {@code "b"}, or {@code "c"}. The hyphen
 * ({@code -}) may be used to specify a range so {@code [a-z]} specifies a range that matches from {@code "a"} to
 * {@code "z"} (inclusive). These forms can be mixed so [abce-g] matches {@code "a"}, {@code "b"}, {@code "c"},
 * {@code "e"}, {@code "f"} or {@code "g"}. If the character after the {@code [} is a {@code !} then it is used for
 * negation so {@code
 *   [!a-c]} matches any character except {@code "a"}, {@code "b"}, or {@code
 *   "c"}.
 * <p>
 * Within a bracket expression the {@code *}, {@code ?} and {@code \} characters match themselves. The ({@code -})
 * character matches itself if it is the first character within the brackets, or the first character after the {@code !}
 * if negating.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The {@code { }} characters are a group of subpatterns, where the group matches if any subpattern in the group
 * matches. The {@code ","} character is used to separate the subpatterns. Groups cannot be nested.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * Leading period<code>&#47;</code>dot characters in file name are treated as regular characters in match operations.
 * For example, the {@code "*"} glob pattern matches file name {@code ".login"}. The {@link Files#isHidden} method may
 * be used to test whether a file is considered hidden.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * All other characters match themselves in an implementation dependent manner. This includes characters representing
 * any {@link FileSystem#getSeparator name-separators}.
 * </p>
 * </li>
 *
 * <li>
 * <p>
 * The matching of {@link Path#getRoot root} components is highly implementation-dependent and is not specified.
 * </p>
 * </li>
 *
 * </ul>
 */

@SpringBootTest
public class TestPathMatcher
{
@Test
public void testVrouwenSlashSterSter()
{
	@SuppressWarnings( "resource" )
	PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher( "glob:/home/purbanus/Videos/vrouwen/**" );
	assertFalse( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen" ) ) );
	assertFalse( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anyfile" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anydir/anyfile" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anydir/otherdir/anyfile" ) ) );
}
@Test
public void testVrouwenSterSter()
{
	@SuppressWarnings( "resource" )
	PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher( "glob:/home/purbanus/Videos/vrouwen**" );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anyfile" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anydir/anyfile" ) ) );
	assertTrue( pathMatcher.matches( Paths.get( "/home/purbanus/Videos/vrouwen/anydir/otherdir/anyfile" ) ) );
}

}
