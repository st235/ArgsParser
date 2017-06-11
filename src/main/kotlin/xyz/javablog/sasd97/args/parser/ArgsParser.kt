package xyz.javablog.sasd97.args.parser

import xyz.javablog.sasd97.args.parser.analytics.ArgumentsAnalyzer
import xyz.javablog.sasd97.args.parser.analytics.converters.CommandFlagToKeyConverter
import xyz.javablog.sasd97.args.parser.analytics.verifiers.CommandFlagVerifier
import xyz.javablog.sasd97.args.parser.commons.Analyzer
import xyz.javablog.sasd97.args.parser.commons.Defaults.COMMAND_ARGUMENT_FLAG
import xyz.javablog.sasd97.args.parser.commons.Defaults.LEXEME_DELIMITER
import xyz.javablog.sasd97.args.parser.commons.Defaults.TYPE_KEY_DELIMITER
import xyz.javablog.sasd97.args.parser.syntax.SchemeAnalyzer
import xyz.javablog.sasd97.args.parser.syntax.converters.TokenConverter
import xyz.javablog.sasd97.args.parser.syntax.separators.LexemeMultiSeparator
import xyz.javablog.sasd97.args.parser.syntax.separators.TokensPairSeparator

/**
 * Created by alexander on 10/06/2017.
 */

open class ArgsParser private constructor(builder: Builder) {

    private var lexemeDelimiter: String
    private var typeKeyDelimiter: String
    private var commandArgumentFlag: String
    private var schemeAnalyzer: Analyzer<String, TokenConverter<Any>?>
    private var argumentsAnalyzer: Analyzer<String, Any?>

    init {
        lexemeDelimiter = builder.lexemeDelimiter
        typeKeyDelimiter = builder.typeKeyDelimiter
        commandArgumentFlag = builder.commandArgumentFlag

        schemeAnalyzer = SchemeAnalyzer(scheme,
                LexemeMultiSeparator(lexemeDelimiter),
                TokensPairSeparator(typeKeyDelimiter)).makeAnalyze()

        argumentsAnalyzer = ArgumentsAnalyzer(args,
                CommandFlagVerifier(commandArgumentFlag),
                CommandFlagToKeyConverter(commandArgumentFlag),
                schemeAnalyzer).makeAnalyze()
    }

    protected fun getKeyConverter(key: String) = schemeAnalyzer.getAnalyzed(key)

    public fun get(key: String): Any {
        val value = argumentsAnalyzer.getAnalyzed(key)
        if (value == null) throw IllegalStateException("There is cannot be null values in map by key $key")
        return value
    }

    companion object Builder {
        private var lexemeDelimiter: String = LEXEME_DELIMITER
        private var typeKeyDelimiter: String = TYPE_KEY_DELIMITER
        private var commandArgumentFlag: String = COMMAND_ARGUMENT_FLAG

        private lateinit var scheme: String
        private lateinit var args: Array<String>

        public fun setLexemeDelimiter(lexemeDelimiter: String): Builder {
            this.lexemeDelimiter = lexemeDelimiter
            return this
        }

        public fun setTypeKeyDelimiter(typeKeyDelimiter: String): Builder {
            this.typeKeyDelimiter = typeKeyDelimiter
            return this
        }

        public fun setCommandArgumentFlag(commandArgumentFlag: String): Builder {
            this.commandArgumentFlag = commandArgumentFlag
            return this
        }

        public fun setScheme(scheme: String): Builder {
            this.scheme = scheme
            return this
        }

        public fun setArgs(args: Array<String>): Builder {
            this.args = args
            return this
        }

        public fun build(): ArgsParser = ArgsParser(this)
    }
}
